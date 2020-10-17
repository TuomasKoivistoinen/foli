(ns foli.navigation
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

(fp/defsc MuiBottomNavigation
  [this {:keys [value]}]
  {:initial-state (fn [_] {:value 30})
   :ident         (fn [] [::id "singleton"])
   :query         [:value]
   :use-hooks?    true}
  (let [^Object classes (use-styles)
        margin (.-margin classes)
        [value1 setValue1] (hooks/use-state 0)
        [value2 setValue2] (hooks/use-state 0)]
    (mui/mui-container
      {}
      (ui-theme-provider
        {:theme theme}
        (mui/mui-bottom-navigation
          {:key        1
           :className  margin
           :value      value1
           :onChange   (fn [_ newValue] (setValue1 newValue))
           :showLabels true
           :style      {:width            500
                        :backgroundColor "#EFEFEF"}}
          (mui/mui-bottom-navigation-action
            {:key   1
             :label "Favorites"
             :icon  (icons/favorite)})
          (mui/mui-bottom-navigation-action
            {:key   2
             :label "New"
             :icon  (icons/add)})
          (mui/mui-bottom-navigation-action
            {:key   3
             :label "Navigate"
             :icon  (icons/navigation)}))
        (mui/mui-bottom-navigation
          {:key       2
           :className margin
           :value     value2
           :onChange  (fn [_ newValue] (setValue2 newValue))

           :style     {:width            500
                       :background-color "#EFEFEF"}}
          (mui/mui-bottom-navigation-action
            {:key   1
             :label "Favorites"
             :icon  (icons/favorite)})
          (mui/mui-bottom-navigation-action
            {:key   2
             :label "New"
             :icon  (icons/add)})
          (mui/mui-bottom-navigation-action
            {:key   3
             :label "Navigate"
             :icon  (icons/navigation)})
          (mui/mui-bottom-navigation-action
            {:key   4
             :label "Deleted"
             :icon  (icons/delete)}))))))

(ws/defcard mui-bottom-navigation-card
  {::wsm/card-width  7
   ::wsm/card-height 10}
  (ct.fulcro/fulcro-card
    {::ct.fulcro/root       MuiBottomNavigation
     ::ct.fulcro/wrap-root? true}))

(fp/defsc MuiBreadCrumbs
  [this {:keys [value]}]
  {:initial-state (fn [_] {:value 30})
   :ident         (fn [] [::id "singleton"])
   :query         [:value]
   :use-hooks?    true}
  (let [^Object classes (use-styles)
        icons (.-icons classes)
        [value1 setValue1] (hooks/use-state 0)
        [value2 setValue2] (hooks/use-state 0)]
    (ui-theme-provider
      {:theme theme}
      (mui/mui-container
        {:style {:width 400}}
        (mui/mui-breadcrumbs
          {}
          (mui/mui-link {} "Home")
          (mui/mui-link {} "Settings")
          (mui/mui-link {:color "textPrimary"} "Email")))
      (mui/mui-container
        {:style {:width 400}}
        (mui/mui-breadcrumbs
          {:separator ">"}
          (mui/mui-link
            {:style {:display "flex"}}
            (icons/home {:className icons}) "Home")
          (mui/mui-link
            {:style {:display "flex"}}
            (icons/settings {:className icons}) "Settings")
          (mui/mui-link
            {:color "textPrimary"
             :style {:display "flex"}}
            (icons/email {:className icons}) "Email"))))))

(ws/defcard mui-breadcrumbs-card
  {::wsm/card-width  7
   ::wsm/card-height 10}
  (ct.fulcro/fulcro-card
    {::ct.fulcro/root       MuiBreadCrumbs
     ::ct.fulcro/wrap-root? true}))

(defn side-nav [props]
  (mui/mui-list
    props
    (mui/mui-list-item
      {:button true :key 1}
      (mui/mui-list-item-icon {} (icons/move-to-inbox))
      (mui/mui-list-item-text {:primary "Inbox"}))
    (mui/mui-list-item
      {:button true :key 2}
      (mui/mui-list-item-icon {} (icons/mail))
      (mui/mui-list-item-text {:primary "Mail"}))
    (mui/mui-list-item
      {:button true :key 3}
      (mui/mui-list-item-icon {} (icons/delete))
      (mui/mui-list-item-text {:primary "Thrash"}))
    (mui/mui-divider)
    (mui/mui-list-item
      {:button true :key 4}
      (mui/mui-list-item-icon {} (icons/settings))
      (mui/mui-list-item-text {:primary "Settings"}))))

(fp/defsc MuiTemporaryDrawers
  [this {:keys [value]}]
  {:initial-state (fn [_] {:value 30})
   :ident         (fn [] [::id "singleton"])
   :query         [:value]
   :use-hooks?    true}
  (let [^Object classes (use-styles)
        icons (.-icons classes)
        [open1 setOpen1] (hooks/use-state false)
        [open2 setOpen2] (hooks/use-state false)
        [open3 setOpen3] (hooks/use-state false)
        [open4 setOpen4] (hooks/use-state false)]
    (ui-theme-provider
      {:theme theme}
      (mui/mui-container
        {}
        (mui/mui-button
          {:onClick (fn [] (setOpen1 true))
           :variant "contained"
           :color   "primary"}
          "Left")
        (mui/mui-button
          {:onClick (fn [] (setOpen2 true))
           :variant "contained"
           :color   "primary"}
          "Right")
        (mui/mui-button
          {:onClick (fn [] (setOpen3 true))
           :variant "contained"
           :color   "primary"}
          "Top")
        (mui/mui-button
          {:onClick (fn [] (setOpen4 true))
           :variant "contained"
           :color   "primary"}
          "Bottom")
        (mui/mui-drawer
          {:anchor  "left"
           :open    open1
           :onClose (fn [] (setOpen1 false))}
          (side-nav {:style {:width 250}}))
        (mui/mui-drawer
          {:anchor  "right"
           :open    open2
           :onClose (fn [] (setOpen2 false))}
          (side-nav {:style {:width 250}}))
        (mui/mui-drawer
          {:anchor  "top"
           :open    open3
           :onClose (fn [] (setOpen3 false))}
          (side-nav {:style {:width "auto"}}))
        (mui/mui-drawer
          {:anchor  "bottom"
           :open    open4
           :onClose (fn [] (setOpen4 false))}
          (side-nav {:style {:width "auto"}}))))))

(ws/defcard mui-drawers-temporary-card
  {::wsm/card-width  7
   ::wsm/card-height 10}
  (ct.fulcro/fulcro-card
    {::ct.fulcro/root       MuiTemporaryDrawers
     ::ct.fulcro/wrap-root? true}))

(fp/defsc MuiPersistentDrawers
  [this {:keys [value]}]
  {:initial-state (fn [_] {:value 30})
   :ident         (fn [] [::id "singleton"])
   :query         [:value]
   :use-hooks?    true}
  (let [^Object classes (use-styles)
        icons (.-icons classes)
        [open1 setOpen1] (hooks/use-state false)
        [open2 setOpen2] (hooks/use-state false)]
    (ui-theme-provider
      {:theme theme}
      (mui/mui-container
        {}
        (mui/mui-button
          {:onClick (fn [] (setOpen1 (not open1)))
           :variant "contained"
           :color   "primary"}
          "Toggle left")
        (mui/mui-button
          {:onClick (fn [] (setOpen2 (not open2)))
           :variant "contained"
           :color   "primary"}
          "Toggle right")
        (mui/mui-drawer
          {:anchor  "left"
           :variant "persistent"
           :open    open1
           :onClose (fn [] (setOpen1 false))}
          (side-nav {:style {:width 250}}))
        (mui/mui-drawer
          {:anchor  "right"
           :variant "persistent"
           :open    open2
           :onClose (fn [] (setOpen2 false))}
          (side-nav {:style {:width 250}}))))))

(ws/defcard mui-drawers-persistent-card
  {::wsm/card-width  7
   ::wsm/card-height 10}
  (ct.fulcro/fulcro-card
    {::ct.fulcro/root       MuiPersistentDrawers
     ::ct.fulcro/wrap-root? true}))

(fp/defsc MuiMenus
  [this {:keys [value]}]
  {:initial-state (fn [_] {:value 30})
   :ident         (fn [] [::id "singleton"])
   :query         [:value]
   :use-hooks?    true}
  (let [[anchor1 setAnchor1] (hooks/use-state nil)]
    (ui-theme-provider
      {:theme theme}
      (mui/mui-container
        {}
        (mui/mui-button
          {:onClick (fn [evt] (setAnchor1 (.-currentTarget evt)))
           :variant "contained"
           :color   "primary"}
          "Open menu")
        (mui/mui-menu
          {:anchorEl    anchor1
           :keepMounted true
           :open        (boolean anchor1)
           :onClose     (fn [] (setAnchor1 nil))}
          (mui/mui-menu-item {} "Profile")
          (mui/mui-menu-item {} "My account")
          (mui/mui-menu-item {} "Logout"))))))

(ws/defcard mui-menus-card
  {::wsm/card-width  7
   ::wsm/card-height 10}
  (ct.fulcro/fulcro-card
    {::ct.fulcro/root       MuiMenus
     ::ct.fulcro/wrap-root? true}))

(fp/defsc MuiSteppers
  [this {:keys [value]}]
  {:initial-state (fn [_] {:value 30})
   :ident         (fn [] [::id "singleton"])
   :query         [:value]
   :use-hooks?    true}
  (let [[step1 setStep1] (hooks/use-state 0)
        [step2 setStep2] (hooks/use-state 0)]
    (ui-theme-provider
      {:theme theme}
      (mui/mui-container
        {}
        (mui/mui-stepper
          {:activeStep step1}
          (map-indexed (fn [idx s]
                         (mui/mui-step
                           {:key     s
                            :onClick (fn [] (setStep1 idx))}
                           (mui/mui-step-label {} s)))
                       ["Step 1" "Step 2" "Step 3"]))
        (mui/mui-grid
          {:container true}
          (mui/mui-grid
            {:item true
             :xs   true}
            (when (> step1 0)
              (mui/mui-button
                {:variant "contained"
                 :color   "secondary"
                 :onClick (fn [] (and (> step1 0) (setStep1 (dec step1))))}
                "Previous")))
          (mui/mui-grid
            {:item  true
             :xs    true
             :style {:textAlign "right"}}
            (mui/mui-button
              {:variant "contained"
               :color   "primary"
               :onClick (fn [] (and (< step1 3) (setStep1 (inc step1))))}
              (if (< step1 3)
                "Next"
                "Submit"))))
        (mui/mui-stepper
          {:activeStep       step2
           :alternativeLabel true}
          (map-indexed (fn [idx s]
                         (mui/mui-step
                           {:key     s
                            :onClick (fn [] (setStep2 idx))}
                           (mui/mui-step-label {} s)))
                       ["Step 1" "Step 2" "Step 3"]))
        (mui/mui-grid
          {:container true}
          (mui/mui-grid
            {:item true
             :xs   true}
            (when (> step2 0)
              (mui/mui-button
                {:variant "contained"
                 :color   "secondary"
                 :onClick (fn [] (and (> step2 0) (setStep2 (dec step2))))}
                "Previous")))
          (mui/mui-grid
            {:item  true
             :xs    true
             :style {:textAlign "right"}}
            (mui/mui-button
              {:variant "contained"
               :color   "primary"
               :onClick (fn [] (and (< step2 3) (setStep2 (inc step2))))}
              (if (< step2 3)
                "Next"
                "Submit"))))))))

(ws/defcard mui-steppers-card
  {::wsm/card-width  7
   ::wsm/card-height 10}
  (ct.fulcro/fulcro-card
    {::ct.fulcro/root       MuiSteppers
     ::ct.fulcro/wrap-root? true}))

(fp/defsc MuiTabs
  [this {:keys [value]}]
  {:initial-state (fn [_] {:value 30})
   :ident         (fn [] [::id "singleton"])
   :query         [:value]
   :use-hooks?    true}
  (let [[tab1 setTab1] (hooks/use-state 0)
        [tab2 setTab3] (hooks/use-state 0)]
    (ui-theme-provider
      {:theme theme}
      (mui/mui-container
        {}
        (mui/mui-app-bar
          {:position "static"}
          (mui/mui-tabs
            {:value    tab1
             :onChange (fn [_ newValue] (setTab1 newValue))}
            (mui/mui-tab {:label "Tab 1"})
            (mui/mui-tab {:label "Tab 2"})
            (mui/mui-tab {:label "Tab 3"})))
        (dom/div
          {:style {:backgroundColor "#e5e5e5"
                   :height          "200px"}}
          (mui/mui-typography {} (str "Tab " (inc tab1))))))))

(ws/defcard mui-tabs-card
  {::wsm/card-width  7
   ::wsm/card-height 10}
  (ct.fulcro/fulcro-card
    {::ct.fulcro/root       MuiTabs
     ::ct.fulcro/wrap-root? true}))