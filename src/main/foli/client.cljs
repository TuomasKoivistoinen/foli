(ns foli.client
  (:require
    [foli.application :refer [app]]
    [foli.root :refer [Root]]
    [com.fulcrologic.fulcro.application :as application]))

(defn ^:export init []
  "On shadow-cljs init"
  (application/mount! app Root "foli")
  (js/console.log "Loaded"))

(defn ^:export refresh []
  "On shadow-cljs hot reload"
  (application/mount! app Root "foli")
  (js/console.log "Hot reload"))
