(ns foli.root
  (:require
    [com.fulcrologic.fulcro.components :as comp]
    [foli.mui :as mui]
    [foli.components.stops :refer [StopList ui-stop-list]]
    [foli.components.trips :refer [TripList ui-trip-list]]
    [com.fulcrologic.fulcro.components :as comp]))

(comp/defsc Root [this {:root/keys [stop-list trip-list] :as props}]
  {:query         [{:root/stop-list (comp/get-query StopList)}
                   {:root/trip-list (comp/get-query TripList)}
                   {[:ui :selections] [:stop]}]
   :initial-state (fn [_] {:root/stop-list (comp/get-initial-state StopList)
                           :root/trip-list (comp/get-initial-state TripList)})}
  (mui/ui-theme-provider
    {:theme mui/theme}
    (ui-stop-list stop-list)
    (when-let [selected-stop-id (-> props
                                    (get-in [[:ui :selections] :stop])
                                    second)]
      (ui-trip-list (assoc trip-list :trip-list/id selected-stop-id)))))