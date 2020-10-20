(ns foli.parser
  (:require
    [com.wsscode.pathom.core :as p]
    [com.wsscode.pathom.connect :as pc]
    [com.wsscode.pathom.parser :as pp]
    [foli.models.stops :as stops]
    [foli.models.trips :as trips]
    [next.jdbc :as jdbc]))

(pc/defresolver index-explorer [env _]
  {::pc/input  #{:com.wsscode.pathom.viz.index-explorer/id}
   ::pc/output [:com.wsscode.pathom.viz.index-explorer/index]}
  {:com.wsscode.pathom.viz.index-explorer/index
   (get env ::pc/indexes)})

(def resolvers [index-explorer stops/resolvers trips/resolvers])

(def pathom-parser
  (p/parser
    {::pp/timeout-reach 60000
     ::p/env            {::p/reader                 [p/map-reader
                                                     pc/open-ident-reader
                                                     pc/reader2
                                                     p/env-placeholder-reader]
                         ::pc/mutation-join-globals [:tempids]
                         ::p/placeholder-prefixes   #{">"}
                         ::p/process-error          (fn [_ err] (println err) (p/error-message err))}
     ::p/mutate         pc/mutate
     ::p/plugins        [(pc/connect-plugin {::pc/register resolvers})
                         (p/env-wrap-plugin (fn [env]
                                              (let [ds (jdbc/get-datasource {:dbtype   "postgresql"
                                                                             :user     "postgres"
                                                                             :dbname   "foli"
                                                                             :password "example"})]
                                                (assoc env :ds ds))))
                         (p/post-process-parser-plugin p/elide-not-found)
                         p/error-handler-plugin]}))

(comment

  (time
    (do
      (first (pathom-parser {} [{[:stop/id "53"]
                                 [:stop/name
                                  {:stop/departures
                                   [:departure/id
                                    :departure/headsign
                                    :departure/arrival-time
                                    :departure/departure-time
                                    {:departure/route
                                     [:route/id
                                      :route/color
                                      :route/name
                                      :route/color
                                      :route/text-color]}]}]}]))))
  )