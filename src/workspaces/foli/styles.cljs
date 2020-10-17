(ns foli.styles
  (:require
    [com.fulcrologic.fulcro.algorithms.react-interop :as interop]
    ["@material-ui/core/styles" :refer [createMuiTheme makeStyles ThemeProvider]]
    [clojure.string :as string]))

(def ui-theme-provider (interop/react-factory ThemeProvider))

(def theme (createMuiTheme
             (clj->js
               {:palette {:primary   {:main "#ab003c"}
                          :secondary {:main "#33c9dc"}}})))

(defn make-styles [style-function]
  (makeStyles (fn [^Object theme]
                (clj->js (style-function (js->clj theme))))))

(def use-styles (makeStyles
                  (fn [^Object theme]
                    (clj->js {:margin       {:margin (^js .spacing theme 1)}
                              :padding      {:padding (^js .spacing theme 1)}
                              :formControl  {:minWidth 240
                                             :margin   (^js .spacing theme 1)}
                              :customButton {:background   "linear-gradient(45deg, #2196F3 30%, #21CBF3 90%)"
                                             :border       0
                                             :borderRadius 3
                                             :boxShadow    "0 3px 5px 2px rgba(33, 203, 243, .3)"
                                             :color        "black"
                                             :height       48
                                             :padding      "0 30px"}
                              :icons        {:marginRight (^js .spacing theme 0.5)
                                             :marginTop   (^js .spacing theme 0.3)
                                             :width       21
                                             :height      21}
                              :backdrop     {:zIndex (+ 1 (^js .. theme -zIndex -drawer))
                                             :color  "#fff"}}))))