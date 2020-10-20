(ns foli.components.stops
  (:require
    [com.fulcrologic.fulcro.components :as comp]
    [com.fulcrologic.fulcro.mutations :as m]
    [foli.mui :as mui]
    [com.fulcrologic.fulcro.dom :as dom]
    [com.fulcrologic.fulcro.data-fetch :as df]
    [edn-query-language.core :as eql]))

(comp/defsc RouteDisplay [_ {:route/keys [id name]}]
  {:ident :route/id
   :query [:route/id :route/name :route/color :route/text-color]}
  (mui/typography
    {:variant "subtitle1"}
    id " / " name))

(def ui-route-display (comp/factory RouteDisplay))

(comp/defsc DepartureDisplay [_ {:departure/keys [headsign departure-time route]}]
  {:ident :departure/id
   :query [:departure/id :departure/headsign :departure/departure-time
           {:departure/route (comp/get-query RouteDisplay)}]}
  (let [{:route/keys [color text-color]} route]
    (mui/card
      {:style {:margin          "8px"
               :maxWidth        "300px"
               :color           (str "#" text-color)
               :backgroundColor (str "#" color)}}
      (mui/card-content
        {}
        (ui-route-display route)
        (mui/typography
          {:variant "h6"}
          headsign)
        (mui/typography
          {:variant "h6"}
          departure-time)))))

(def ui-departure-display (comp/factory DepartureDisplay {:keyfn :departure/id}))

(comp/defsc StopDisplay [_ {:stop/keys [id name departures]}]
  {:ident :stop/id
   :query [:stop/id :stop/name
           {:stop/departures (comp/get-query DepartureDisplay)}]}
  (comp/fragment
    (mui/typography {:variant "h5"} id " " name)
    {}
    (->> departures
         (sort-by :departure/departure-time)
         (mapv (fn [departure]
                 (ui-departure-display departure))))))

(def ui-stop-display (comp/factory StopDisplay))

(defn update-key-params
  "Not for nested children"
  [the-key params]
  (fn [query]
    (-> (eql/query->ast query)
        (update :children (fn [children]
                            (mapv (fn [child]
                                    (if (= (:key child) the-key)
                                      (assoc child :params params)
                                      child))
                                  children)))
        (eql/ast->query))))

(comment
  (let [query [:stop/id
               {:stop/departures
                [:departure/id
                 {:departure/route
                  [:route/id]}]}]
        update-query-fn (update-key-params :stop/departures {:extra "params"})]
    (update-query-fn query)))

(m/defmutation select-stop [{:stop/keys [id]}]
  (action
    [{:keys [app state]}]
    (swap! state assoc-in [:component/id :stop-selection :stop-selection/stop] [:stop/id id])
    (df/load! app [:stop/id id] StopDisplay {:params       {:departures/since (js/Date.)}
                                             :update-query (update-key-params :stop/departures {:since (js/Date.)})})))

(comp/defsc StopSelection [this {:ui/keys             [input-text]
                                 :stop-selection/keys [stop]}]
  {:query         [:ui/input-text
                   {:stop-selection/stop (comp/get-query StopDisplay)}]
   :ident         (fn [] [:component/id :stop-selection])
   :initial-state (fn [_] {:ui/input-text       ""
                           :stop-selection/stop (comp/initial-state StopDisplay {})})}
  (mui/container
    {}
    (mui/typography {:variant "h5"} "Pysäkin valinta")
    (dom/form
      {
       :onSubmit (fn [evt]
                   (.preventDefault evt)
                   (comp/transact! this [(select-stop {:stop/id input-text})]))}
      (mui/text-field
        {:variant  "outlined"
         :label    "Pysäkin numero"
         :value    input-text
         :onChange (fn [evt] (m/set-string! this :ui/input-text :event evt))})
      (mui/button
        {:type    "submit"
         :variant "contained"}
        "Valitse pysäkki"))
    (when (:stop/id stop)
      (ui-stop-display stop))))

(def ui-stop-selection (comp/factory StopSelection))