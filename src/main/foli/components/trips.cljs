(ns foli.components.trips
  (:require
    [clojure.string :as string]
    [com.fulcrologic.fulcro.components :as comp]
    [com.fulcrologic.fulcro.mutations :as m]
    [foli.mui :as mui]))

(def trip-id (atom 0))

(comp/defsc Trip [_ _]
  {:query         [:trip/id :trip/name]
   :ident         :trip/id
   :initial-state (fn [{:keys [name]}] {:trip/id (swap! trip-id inc) :trip/name name})})

(comp/defsc TripList [this {:trip-list/keys [id trips] :as props}]
  {:query         [:trip-list/id {:trip-list/trips (comp/get-query Trip)}]
   :ident         :trip-list/id
   :initial-state (fn [_] {:trip-list/trips [(comp/get-initial-state Trip {:name "eka"})
                                             (comp/get-initial-state Trip {:name "toka"})]})}
  (mui/container
    {}
    (mui/typography {:variant "h5"} "Pysäkin matkat")
    (mui/list
      {}
      (->> trips
           (mapv (fn [{:trip/keys [id name]}]
                   (comp/fragment
                     (mui/list-item
                       {:id id}
                       (mui/list-item-text {} id ": " name))
                     (mui/divider))))))))

(def ui-trip-list (comp/factory TripList))