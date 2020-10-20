(ns foli.root
  (:require
    [com.fulcrologic.fulcro.components :as comp]
    [foli.mui :as mui]
    [foli.components.stops :refer [StopSelection ui-stop-selection]]
    [com.fulcrologic.fulcro.components :as comp]))

(comp/defsc Root [_ {:root/keys [stop-selection]}]
  {:query         [{:root/stop-selection (comp/get-query StopSelection)}]
   :initial-state (fn [_]
                    {:root/stop-selection (comp/get-initial-state StopSelection)})}
  (mui/ui-theme-provider
    {:theme mui/theme}
    (ui-stop-selection stop-selection)))