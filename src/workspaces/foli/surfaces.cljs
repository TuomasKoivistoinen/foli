(ns foli.surfaces
  (:require [com.fulcrologic.fulcro.components :as fp]
            [com.fulcrologic.fulcro.mutations :as fm]
            [com.fulcrologic.fulcro.react.hooks :as hooks]
            [nubank.workspaces.core :as ws]
            [nubank.workspaces.model :as wsm]
            [nubank.workspaces.card-types.fulcro3 :as ct.fulcro]
            [foli.styles :refer [ui-theme-provider use-styles theme]]
            [foli.mui-components :as mui]
            [foli.ui-icons :as icons]
            [com.fulcrologic.fulcro.dom :as dom]))

(fp/defsc MuiAppBars
  [this {:keys [value]}]
  {:initial-state (fn [_] {:value 30})
   :ident         (fn [] [::id "singleton"])
   :query         [:value]
   :use-hooks?    true}
  (let [^Object classes (use-styles)]
    (ui-theme-provider
      {:theme theme}
      (mui/mui-container
        {:style {:flexGrow 1
                 :width    "100vw"}}
        (mui/mui-app-bar
          {:position "static"}
          (mui/mui-toolbar
            {}
            (mui/mui-icon-button
              {:edge "start" :style {:marginRight "20px"}}
              (icons/menu))
            (mui/mui-typography
              {:variant "h6" :style {:flexGrow 1}}
              "News")
            (mui/mui-button {} "Login"))))
      (mui/mui-container
        {:style {:flexGrow 1
                 :width    "100vw"}}
        (mui/mui-app-bar
          {:position "static"
           :color    "secondary"}
          (mui/mui-toolbar
            {}
            (mui/mui-icon-button
              {:edge "start" :style {:marginRight "20px"}}
              (icons/menu))
            (mui/mui-typography
              {:variant "h6" :style {:flexGrow 1}}
              "News")
            (mui/mui-button {} "Login")))))))

(ws/defcard mui-app-bars-card
  {::wsm/card-width  7
   ::wsm/card-height 10}
  (ct.fulcro/fulcro-card
    {::ct.fulcro/root       MuiAppBars
     ::ct.fulcro/wrap-root? true}))

(def paper-key (atom 0))

(fp/defsc MuiPapers
  [this {:keys [value]}]
  {:initial-state (fn [_] {:value 30})
   :ident         (fn [] [::id "singleton"])
   :query         [:value]
   :use-hooks?    true}
  (let [^Object classes (use-styles)
        paper-style {:margin "20px"
                     :width  "200px"}]
    (ui-theme-provider
      {:theme theme}
      (mui/mui-container
        {}
        (mui/mui-paper
          {:elevation 1
           :style     paper-style}
          (mui/mui-typography {:key (swap! paper-key inc)} "Elevation 1")
          (mui/mui-typography {:key (swap! paper-key inc)} "Elevation 1")
          (mui/mui-typography {:key (swap! paper-key inc)} "Elevation 1"))
        (mui/mui-paper
          {:elevation 2
           :style     paper-style}
          (mui/mui-typography {:key (swap! paper-key inc)} "Elevation 2")
          (mui/mui-typography {:key (swap! paper-key inc)} "Elevation 2")
          (mui/mui-typography {:key (swap! paper-key inc)} "Elevation 2"))
        (mui/mui-paper
          {:elevation 3
           :style     paper-style}
          (mui/mui-typography {:key (swap! paper-key inc)} "Elevation 3")
          (mui/mui-typography {:key (swap! paper-key inc)} "Elevation 3")
          (mui/mui-typography {:key (swap! paper-key inc)} "Elevation 3")))
      (mui/mui-container
        {}
        (mui/mui-paper
          {:variant "outlined"
           :style   paper-style}
          (mui/mui-typography {:key (swap! paper-key inc)} "Outlined")
          (mui/mui-typography {:key (swap! paper-key inc)} "Outlined")
          (mui/mui-typography {:key (swap! paper-key inc)} "Outlined"))
        (mui/mui-paper
          {:square  true
           :variant "outlined"
           :style   paper-style}
          (mui/mui-typography {:key (swap! paper-key inc)} "Outlined square")
          (mui/mui-typography {:key (swap! paper-key inc)} "Outlined square")
          (mui/mui-typography {:key (swap! paper-key inc)} "Outlined square"))))))

(ws/defcard mui-papers-card
  {::wsm/card-width  7
   ::wsm/card-height 12}
  (ct.fulcro/fulcro-card
    {::ct.fulcro/root       MuiPapers
     ::ct.fulcro/wrap-root? true}))

(fp/defsc MuiCards
  [this {:keys [value]}]
  {:initial-state (fn [_] {:value 30})
   :ident         (fn [] [::id "singleton"])
   :query         [:value]
   :use-hooks?    true}
  (let [^Object classes (use-styles)]
    (ui-theme-provider
      {:theme theme}
      (mui/mui-container
        {:style {:minWidth 275}}
        (mui/mui-card
          {}
          (mui/mui-card-content
            {}
            (mui/mui-typography
              {:variant "body2"}
              "Text content"))
          (mui/mui-card-actions
            {}
            (mui/mui-button {} "Learn more")))
        (mui/mui-card
          {:style {:marginTop "20px"}}
          (mui/mui-card-header
            {:avatar    (mui/mui-avatar {} "R")
             :action    (mui/mui-icon-button {} (icons/more-vert))
             :title     "Title"
             :subheader "Subheader"})
          (mui/mui-card-media
            {:style {:height 0 :paddingTop "56.25%"}
             :image "https://media.sproutsocial.com/uploads/2017/02/10x-featured-social-media-image-size.png"
             :title "Image"})
          (mui/mui-card-content
            {}
            (mui/mui-typography
              {:variant "body2"}
              "Text content"))
          (mui/mui-card-actions
            {:disableSpacing true}
            (mui/mui-icon-button {} (icons/share))
            (mui/mui-icon-button {} (icons/favorite))
            (mui/mui-icon-button {:style {:marginLeft "auto"}} (icons/expand-more))))))))

(ws/defcard mui-cards-card
  {::wsm/card-width  7
   ::wsm/card-height 14}
  (ct.fulcro/fulcro-card
    {::ct.fulcro/root       MuiCards
     ::ct.fulcro/wrap-root? true}))