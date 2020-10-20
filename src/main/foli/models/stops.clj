(ns foli.models.stops
  (:require
    [com.wsscode.pathom.connect :as pc]
    [next.jdbc :as jdbc]
    [clojure.set :as set])
  (:import (java.util Date)
           (java.text SimpleDateFormat)))

(pc/defresolver stop-list [{:keys [ds]} _]
  {::pc/output [{:stop-list
                 [{:stop-list/stops
                   [:stop/id :stop/name]}]}]}
  (let [stops (jdbc/execute! ds ["SELECT stop_id, stop_name FROM stops"])]
    {:stop-list {:stop-list/stops (mapv
                                    (fn [{:stops/keys [stop_id stop_name]}]
                                      {:stop/id stop_id :stop/name stop_name})
                                    stops)}}))

(def select-stop
  "SELECT stop_name, stop_lon, stop_lat, stop_timezone, zone_id
  FROM stops
  WHERE stop_id = ?")

(pc/defresolver stop-resolver [{:keys [ds] :as env} {stop-id :stop/id}]
  {::pc/input  #{:stop/id}
   ::pc/output [:stop/name :stop/longitude :stop/latitude :stop/timezone :stop/zone]}
  (let [stop (jdbc/execute-one! ds [select-stop stop-id])]
    (set/rename-keys stop {:stops/stop_name :stop/name
                           :stops/stop_lon  :stop/longitude
                           :stops/stop_lat  :stop/latitude
                           :stops/timezone  :stops/stop_timezone
                           :stops/zone_id   :stops/zone})))

(defn parse-date [date]
  (.format (SimpleDateFormat. "yyyyMMdd") date))

(defn parse-time [date]
  (.format (SimpleDateFormat. "HH:mm:ss") date))

(pc/defresolver departure-resolver [{:keys [ds] :as env} {stop-id :stop/id}]
  {::pc/input  #{:stop/id}
   ::pc/output [{:stop/departures
                 [:departure/id :departure/arrival-time :departure/departure-time :departure/trip-id
                  :departure/headsign :departure/route-id]}]}
  (let [params (or (-> env :ast :params) {})
        since (->> (get params :since (new Date))
                   (parse-time))
        now (new Date)
        date-start (-> (get-in env [:ast :params :date/start] now)
                       (parse-date))
        date-end (-> (get-in env [:ast :paramms :date/end] now)
                     (parse-date))
        departures (jdbc/execute! ds ["SELECT st.id, st.arrival_time, st.departure_time, st.trip_id, t.trip_headsign, t.route_id FROM stop_times as st
  JOIN trips as t on t.trip_id = st.trip_id
  JOIN calendar_dates as cd on cd.service_id = t.service_id
  WHERE st.stop_id = ?
  AND cd.date >= ? AND cd.date <= ?
  AND st.departure_time >= ?" stop-id date-start date-end since])]
    {:stop/departures
     (mapv (fn [departure]
             (set/rename-keys departure
                              {:stop_times/id             :departure/id
                               :stop_times/arrival_time   :departure/arrival-time
                               :stop_times/departure_time :departure/departure-time
                               :stop_times/trip_id        :departure/trip-id
                               :trips/trip_headsign       :departure/headsign
                               :trips/route_id            :departure/route-id}))
           departures)}))

(pc/defresolver departure-trip-id-resolver [_ {trip-id :departure/trip-id}]
  {::pc/input  #{:departure/trip-id}
   ::pc/output [{:departure/trip [:trip/id]}]}
  {:departure/trip {:trip/id trip-id}})

(pc/defresolver departure-route-id-resolver [_ {route-id :departure/route-id}]
  {::pc/input  #{:departure/route-id}
   ::pc/output [{:departure/route [:route/id]}]}
  {:departure/route {:route/id route-id}})

(pc/defresolver route-resolver [{:keys [ds]} {route-id :route/id}]
  {::pc/input  #{:route/id}
   ::pc/output [:route/name :route/color :route/text-color]}
  (let [route (jdbc/execute-one! ds ["SELECT route_long_name, route_color, route_text_color FROM routes WHERE route_id = ?" route-id])]
    (set/rename-keys route {:routes/route_long_name  :route/name
                            :routes/route_color      :route/color
                            :routes/route_text_color :route/text-color})))

(pc/defresolver trip-resolver [{:keys [ds]} {trip-id :trip/id}]
  {::pc/input  #{:trip/id}
   ::pc/output [:trip/id :trip/headsign]}
  (let [trip (jdbc/execute-one! ds ["SELECT trip_headsign FROM trips WHERE trip_id = ?" trip-id])]
    (set/rename-keys trip {:trips/trip_headsign :trip/headsign})))

(def resolvers [stop-list
                stop-resolver
                departure-resolver
                departure-trip-id-resolver
                departure-route-id-resolver
                route-resolver
                trip-resolver])

(comment
  (def env {:ast {:params {}}})
  (def stop-id "53")
  (def route-id "1")
  (def ds (jdbc/get-datasource {:dbtype   "postgresql"
                                :user     "postgres"
                                :dbname   "foli"
                                :password "example"}))
  (jdbc/execute-one! ds ["SELECT * FROM stops WHERE stop_id = ?" "53"])
  (jdbc/execute! ds ["SELECT st.id, st.arrival_time, st.departure_time, st.trip_id, t.trip_headsign, t.route_id FROM stop_times as st
  JOIN trips as t on t.trip_id = st.trip_id
  JOIN calendar_dates as cd on cd.service_id = t.service_id
  WHERE st.stop_id = ?
  AND cd.date >= ? AND cd.date <= ?" "53" "20201019" "20201019"])
  (let [params {}
        now (new Date)]
    (-> (get params :date/start now)
        (parse-date)))
  (jdbc/execute-one! ds ["SELECT * FROM routes WHERE route_id = ?" "1"])
  (jdbc/execute-one! ds ["SELECT * FROM trips WHERE trip_id = ?" "00010405__1407generatedBlock"]))