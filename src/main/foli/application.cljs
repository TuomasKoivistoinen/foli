(ns foli.application
  (:require
    [com.fulcrologic.fulcro.application :as app]
    [com.fulcrologic.fulcro.networking.http-remote :refer [fulcro-http-remote]]))

(defonce app (app/fulcro-app {:remotes {:remote (fulcro-http-remote {})}}))