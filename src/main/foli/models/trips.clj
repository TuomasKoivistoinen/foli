(ns foli.models.trips
  (:require
    [com.wsscode.pathom.connect :as pc]
    [next.jdbc :as jdbc])
  (:import (java.text SimpleDateFormat)
           (java.util Date)))

(def todays-trips-by-stop-id
  "SELECT r.route_short_name, r.route_long_name, t.trip_id, t.trip_headsign, s.stop_name, st.trip_id, st.arrival_time, st.departure_time FROM stops AS s
  JOIN stop_times AS st ON s.stop_id = st.stop_id
  JOIN trips AS t ON t.trip_id = st.trip_id
  JOIN calendar AS c ON t.service_id = c.service_id
  JOIN calendar_dates AS cd ON c.service_id = cd.service_id
  JOIN routes AS r ON t.route_id = r.route_id
  WHERE st.stop_id = ?
  AND cd.date = ?")

(def select-stop "SELECT stop_name FROM stops WHERE id = ?")


(def resolvers [])