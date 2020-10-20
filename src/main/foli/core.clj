(ns foli.core
  (:gen-class)
  (:require
    [foli.server :refer [start]]
    [foli.database :refer [init-db!]]))

(defn -main [& _]
  (init-db!)
  (start)
  (println "Started on port" 3000))