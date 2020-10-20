(ns foli.database
  (:require
    [org.httpkit.client :as http]
    [cheshire.core :as json]
    [clojure.java.io :as io]
    [clojure.data.csv :as csv]
    [next.jdbc :as jdbc]
    [ragtime.jdbc :as jdbc2]
    [ragtime.repl :as migrations]
    [clojure.string :as string]
    [next.jdbc.prepare :as prepare])
  (:import (java.util.zip ZipInputStream)))

(def gtfs-zip-url "http://data.foli.fi/gtfs/gtfs.zip")

(def default-config {:dbtype   "postgresql"
             :user     "postgres"
             :dbname   "foli"
             :password "example"})

(defn download-dataset!
  "Downloads the latest Föli gtfs dataset as a .zip from http://data.foli.fi/gtfs/gtfs.zip
  and unpacks the csv (.txt) files to the $PROJECT/gtfs directory"
  []
  (let [stream (-> @(http/get gtfs-zip-url)
                   (:body)
                   (io/input-stream)
                   (ZipInputStream.))]
    (loop []
      (when-let [entry (.getNextEntry stream)]
        (do
          (println "copying" (.getName entry))
          (clojure.java.io/copy stream (clojure.java.io/file (str "gtfs/" (.getName entry))))
          (recur))))))

(defn csv->db!
  "Imports gtfs csv files from $PROJECT/gtfs directory to database.
  You can pass alternative db config"
  ([] (csv->db! nil))
  ([db-config]
   (let [entities  ["agency" "calendar" "calendar_dates" "routes" "shapes" "trips" "stops" "translations" "stop_times"]
         conn (-> (jdbc/get-datasource (or db-config default-config))
                  (jdbc/get-connection))]
     (dorun
       (map (fn transform+load [entity]
              (with-open [reader (io/reader (str "gtfs/" entity ".txt"))]
                (let [csv-data (csv/read-csv reader)
                      headers (first csv-data)
                      rows (rest csv-data)
                      column-string (string/join "," headers)
                      placeholder-string (as-> (count headers) s
                                               (repeat s "?")
                                               (string/join "," s))
                      ps (jdbc/prepare
                           conn [(format "insert into %s (%s) values (%s)" entity column-string placeholder-string)])]
                  (println "loading" entity "of" entities)
                  (dorun
                    (map (fn prepare-batch [row]
                           (prepare/set-parameters ps row)
                           (.addBatch ps))
                         rows))
                  (.executeBatch ps))))
            entities)))))

(defn migrate-db!
  "Run db migrations from $PROJECT/resources/migrations"
  ([] (migrate-db! nil))
  ([config]
   (let [migration-config {:datastore  (jdbc2/sql-database (or config default-config))
                           :migrations (jdbc2/load-resources "migrations")}]
     (migrations/migrate migration-config))))

(defn rollback-db!
  "Rollback db migrations from $PROJECT/resources/migrations"
  ([] (rollback-db! nil))
  ([config]
   (let [migrations (jdbc2/load-resources "migrations")
         config {:datastore  (jdbc2/sql-database (or config default-config))
                 :migrations migrations}]
     (migrations/rollback config (count migrations)))))

(defn init-db!
  "Resets the database, loads the latest gtfs dataset and imports it to the database.
  Accepts db config. TODO upserts / deletes instead of full db wipe?"
  ([] (init-db! nil))
  ([db-config]
   (time
     (do
       (download-dataset!)
       (rollback-db! db-config)
       (migrate-db! db-config)
       (csv->db! db-config)))))

(comment
  (init-db!)
  (download-dataset!)
  (rollback-db!)
  (migrate-db!)
  (csv->db!)
  ;; test reading json data
  (let [latest-dataset (-> @(http/get "http://data.foli.fi/gtfs/")
                           :body
                           (json/decode true)
                           :latest)
        api-root (str "http://data.foli.fi/gtfs/v0/" latest-dataset)]
    (println "The latest dataset is" latest-dataset)
    (println "The correct api root is" api-root)
    (-> @(http/get (str api-root "/stops"))
        :body
        (json/decode true)))
  ;; test downloading csv data
  (let [stream (-> @(http/get "http://data.foli.fi/gtfs/gtfs.zip")
                   (:body)
                   (io/input-stream)
                   (ZipInputStream.))]
    (loop []
      (when-let [entry (.getNextEntry stream)]
        (do
          (println "copying" (.getName entry))
          (clojure.java.io/copy stream (clojure.java.io/file (str "gtfs/" (.getName entry))))
          (recur)))))
  ;; psql migration testing
  (def config {:dbtype   "postgresql"
               :user     "postgres"
               :dbname   "foli"
               :password "example"})
  (let [migration-config {:datastore  (jdbc2/sql-database config)
                          :migrations (jdbc2/load-resources "migrations")}]
    (migrations/migrate migration-config))
  (let [config {:datastore  (jdbc2/sql-database config)
                :migrations (jdbc2/load-resources "migrations")}]
    (migrations/rollback config 9))
  ;; transform + load
  (let [entities ["agency" "calendar" "calendar_dates" "routes" "shapes" "trips" "stops" "translations" "stop_times"]
        conn (-> (jdbc1/get-datasource config)
                 (jdbc1/get-connection))]
    (dorun
      (map (fn transform+load [entity]
             (with-open [reader (io/reader (str "gtfs/" entity ".txt"))]
               (let [csv-data (csv/read-csv reader)
                     headers (first csv-data)
                     rows (rest csv-data)
                     column-string (string/join "," headers)
                     placeholder-string (as-> (count headers) s
                                              (repeat s "?")
                                              (string/join "," s))
                     ps (jdbc1/prepare
                          conn [(format "insert into %s (%s) values (%s)" entity column-string placeholder-string)])]
                 (println "loading" entity "of" entities)
                 (dorun
                   (map (fn prepare-batch [row]
                          (prepare/set-parameters ps row)
                          (.addBatch ps))
                        rows))
                 (.executeBatch ps))))
           entities)))
  ;; query testing
  (def ds (jdbc/get-datasource config))
  (jdbc/execute! ds ["SELECT r.route_short_name, r.route_long_name, t.trip_headsign, st.trip_id, st.arrival_time, st.departure_time FROM stop_times AS st
  JOIN trips AS t ON t.trip_id = st.trip_id
  JOIN calendar AS c ON t.service_id = c.service_id
  JOIN calendar_dates AS cd ON c.service_id = cd.service_id
  JOIN routes AS r ON t.route_id = r.route_id
  WHERE stop_id = ?
  AND cd.date = ?" "53" (.format (java.text.SimpleDateFormat. "yyyyMMdd") (new java.util.Date))])
  (jdbc/execute-one! ds ["SELECT * FROM stops WHERE stop_id = ?" "53"])
  (jdbc/execute! ds ["SELECT st.id, st.arrival_time, st.departure_time FROM stop_times as st
  JOIN trips as t on t.trip_id = st.trip_id
  JOIN calendar_dates as cd on cd.service_id = t.service_id
  WHERE st.stop_id = ?
  AND cd.date >= ? AND cd.date <= ?" "53" "20201019" "20201019"])
  (jdbc/execute! ds ["SELECT t.trip_id, t.trip_headsign, t.route_id FROM trips as t
  JOIN stop_times as st on t.trip_id = st.trip_id
  WHERE st.id = ?" 22430])
  (jdbc/execute! ds ["SELECT * FROM routes WHERE route_id = ?" "1"])
  (jdbc/execute! ds ["SELECT * FROM calendar WHERE service_id = ?" "a-Arki"])
  (jdbc/execute! ds ["SELECT * from calendar_dates WHERE service_id = ? AND date >= ? AND date <= ?"
                     "a-Arki" "20201012" "20201013"])
  (jdbc/execute! ds ["SELECT * FROM routes WHERE route_id = ?" "1"])
  (jdbc/execute! ds ["SELECT stop_id, stop_name FROM stops WHERE stop_id LIKE ?" "10%"])
  (time
    (do
      (jdbc/execute! ds ["select * from stops"])
      nil))
  (time
    (do
      (jdbc/execute! ds ["select * from stop_times where stop_id = '59'"])
      nil))
  )