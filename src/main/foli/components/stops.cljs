(ns foli.components.stops
  (:require
    [clojure.string :as string]
    [com.fulcrologic.fulcro.components :as comp]
    [com.fulcrologic.fulcro.mutations :as m]
    [foli.mui :as mui]))

(m/defmutation select-stop [{:stop/keys [id]}]
  (action
    [{:keys [state]}]
    (swap! state assoc-in [:ui :selections] {:stop [:stop/id id]})))

(def stop-id (atom 0))

(comp/defsc Stop [_ _]
  {:query         [:stop/id :stop/name]
   :ident         :stop/id
   :initial-state (fn [{:keys [name]}] {:stop/id (swap! stop-id inc) :stop/name name})})

(comp/defsc StopList [this {:stop-list/keys [stops]
                            :ui/keys        [input-text] :as props}]
  {:query         [:ui/input-text {:stop-list/stops (comp/get-query Stop)}
                   {[:ui :selections] [:stop]}]
   :ident         (fn [] [:component/id :stop-list])
   :initial-state (fn [_] {:stop-list/stops [(comp/get-initial-state Stop {:name "eka"})
                                             (comp/get-initial-state Stop {:name "toka"})]})}
  (let [selected-stop-id (-> props
                             (get-in [[:ui :selections] :stop])
                             second)]
    (mui/container
      {}
      (mui/typography {:variant "h5"} "Pysäkin valinta")
      (mui/text-field
        {:variant  "outlined"
         :label    "Pysäkin numero tai nimi"
         :value    input-text
         :onChange (fn [evt] (m/set-string! this :ui/input-text :event evt))})
      (mui/list
        {}
        (->> stops
             (filter (fn [{:stop/keys [id name]}]
                       (or
                         (not input-text)
                         (= selected-stop-id id)
                         (string/includes? name input-text)
                         (string/includes? (str id) input-text))))
             (mapv (fn [{:stop/keys [id name]}]
                     (comp/fragment
                       (mui/list-item
                         {:id       id
                          :selected (= id selected-stop-id)
                          :button   true
                          :onClick  #(comp/transact! this [(select-stop {:stop/id id})])}
                         (mui/list-item-text {} id ": " name))
                       (mui/divider)))))))))

(def ui-stop-list (comp/factory StopList))