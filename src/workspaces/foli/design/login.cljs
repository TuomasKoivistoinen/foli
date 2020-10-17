(ns foli.design.login
  (:require [com.fulcrologic.fulcro.components :as fp]
            [com.fulcrologic.fulcro.mutations :as fm]
            [com.fulcrologic.fulcro.react.hooks :as hooks]
            [nubank.workspaces.core :as ws]
            [nubank.workspaces.model :as wsm]
            [nubank.workspaces.card-types.fulcro3 :as ct.fulcro]
            [foli.styles :refer [ui-theme-provider make-styles theme]]
            [foli.mui-components :as mui]
            [foli.ui-icons :as icons]
            [com.fulcrologic.fulcro.dom :as dom]))

(def use-styles
  (make-styles (fn [theme]
                 (let [spacing (get theme "spacing")]
                   {:loginPaper {:padding       (spacing 5)
                                 :display       "flex"
                                 :flexDirection "column"
                                 :alignItems    "center"}
                    :submit     {:marginTop (spacing 2)}}))))

(fp/defsc Login
  [this {:keys [value]}]
  {:initial-state (fn [_] {:value 30})
   :ident         (fn [] [::id "singleton"])
   :query         [:value]
   :use-hooks?    true}
  (let [classes (js->clj (use-styles))
        input-props {:fullWidth true
                     :variant   "outlined"
                     :margin    "normal"}]
    (ui-theme-provider
      {:theme theme}
      (mui/mui-container
        {:component "main"
         :maxWidth  "xs"}
        (mui/mui-paper
          {:className (classes "loginPaper")}
          (mui/mui-typography
            {:variant "h5"}
            "Login")
          (dom/form
            {:onSubmit (fn [evt] (.preventDefault evt))}
            (mui/mui-text-field
              (merge input-props
                     {:placeholder  "Email *"
                      :autoComplete "email"
                      :autoFocus    true}))
            (mui/mui-text-field
              (merge input-props
                     {:placeholder "Password *"
                      :type        "password"}))
            (mui/mui-button
              {:type      "submit"
               :color     "primary"
               :variant   "contained"
               :className (classes "submit")
               :fullWidth true}
              "Login")))))))

(ws/defcard login-card
            {::wsm/card-width  7
             ::wsm/card-height 10}
            (ct.fulcro/fulcro-card
              {::ct.fulcro/root Login}))