(ns foli.server
  (:require
    [foli.parser :refer [pathom-parser]]
    [org.httpkit.server :as http]
    [com.fulcrologic.fulcro.server.api-middleware :as server]
    [ring.middleware.content-type :refer [wrap-content-type]]
    [ring.middleware.resource :refer [wrap-resource]]
    [clojure.string :as string]))

(def ^:private not-found-handler
  (fn [req]
    {:status  200
     :headers {"Content-Type"                "text/html"
               "Access-Control-Allow-Origin" "*"}
     :body    "not found.."}))

(defn wrap-options [handler]
  (fn [req]
    (if (= (:request-method req) :options)
      {:status  200
       :headers {"Access-Control-Allow-Origin"  "*"
                 "Access-Control-Allow-Methods" "*"
                 "Access-Control-Allow-Headers" "*"
                 "Access-Control-Max-Age"       "1728000"}}
      (handler req))))

(defn alert-request [handler]
  (fn [req]
    (println req)
    ;(cast/alert req)
    (handler req)))

(defn all-routes-to-index [handler]
  (fn [{:keys [uri] :as req}]
    (if (or
          (= "/api" uri)
          (string/ends-with? uri ".css")
          (string/ends-with? uri ".map")
          (string/ends-with? uri ".jpg")
          (string/ends-with? uri ".png")
          (string/ends-with? uri ".js")
          (string/ends-with? uri ".eog")
          (string/ends-with? uri ".svg")
          (string/ends-with? uri ".ttf")
          (string/ends-with? uri ".woff"))
      (handler req)
      (handler (assoc req :uri "/index.html")))))

(def middleware
  (-> not-found-handler
      (server/wrap-api {:uri    "/api"
                        :parser (fn [query]
                                  (pathom-parser {} query))})
      (server/wrap-transit-params)
      (server/wrap-transit-response)
      (wrap-resource "public")
      wrap-content-type
      wrap-options
      all-routes-to-index))

(defonce stop-fn (atom nil))

(defn start []
  (reset! stop-fn (http/run-server middleware {:port 3000})))

(defn stop []
  (when @stop-fn
    (@stop-fn)
    (reset! stop-fn nil)))