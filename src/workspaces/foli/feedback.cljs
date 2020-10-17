(ns foli.feedback
  (:require [com.fulcrologic.fulcro.components :as fp]
            [com.fulcrologic.fulcro.mutations :as fm]
            [com.fulcrologic.fulcro.react.hooks :as hooks]
            [nubank.workspaces.core :as ws]
            [nubank.workspaces.model :as wsm]
            [nubank.workspaces.card-types.fulcro3 :as ct.fulcro]
            [foli.styles :refer [ui-theme-provider use-styles theme]]
            [foli.mui-components :as mui]
            [foli.ui-icons :as icons]
            [com.fulcrologic.fulcro.dom :as dom]
            [cljs.core.async :as async]))

(fp/defsc MuiCircularProgresses
  [this {:keys [value]}]
  {:initial-state (fn [_] {:value 30})
   :ident         (fn [] [::id "singleton"])
   :query         [:value]
   :use-hooks?    true}
  (let [^Object classes (use-styles)
        [value setValue] (hooks/use-state 10)]
    (hooks/use-layout-effect (fn []
                               (async/go
                                 []
                                 (async/<! (async/timeout 2000))
                                 (setValue (if (< value 100)
                                             (+ value 10)
                                             0)))
                               (fn []))
                             [value])
    (ui-theme-provider
      {:theme theme}
      (mui/mui-container
        {}
        (mui/mui-circular-progress
          {:color     "primary"
           :className (^String .-margin classes)})
        (mui/mui-circular-progress
          {:color     "secondary"
           :className (^String .-margin classes)})
        (mui/mui-circular-progress
          {:disableShrink true
           :className     (^String .-margin classes)})
        (mui/mui-circular-progress
          {:disableShrink true
           :color         "secondary"
           :className     (^String .-margin classes)})
        (mui/mui-linear-progress
          {:className (^String .-margin classes)})
        (mui/mui-linear-progress
          {:color     "secondary"
           :className (^String .-margin classes)})
        (mui/mui-circular-progress
          {:variant   "static"
           :value     value
           :className (^String .-margin classes)})
        (mui/mui-circular-progress
          {:color     "secondary"
           :variant   "static"
           :value     value
           :className (^String .-margin classes)})
        (mui/mui-linear-progress
          {:variant   "determinate"
           :value     value
           :className (^String .-margin classes)})
        (mui/mui-linear-progress
          {:variant   "determinate"
           :color     "secondary"
           :value     value
           :className (^String .-margin classes)})
        (mui/mui-linear-progress
          {:variant     "buffer"
           :valueBuffer (+ value 15)
           :value       value
           :className   (^String .-margin classes)})
        (mui/mui-linear-progress
          {:variant     "buffer"
           :valueBuffer (+ value 15)
           :color       "secondary"
           :value       value
           :className   (^String .-margin classes)})))))

(ws/defcard mui-circular-progresses-card
  {::wsm/card-width  7
   ::wsm/card-height 10}
  (ct.fulcro/fulcro-card
    {::ct.fulcro/root       MuiCircularProgresses
     ::ct.fulcro/wrap-root? true}))

(def transition-up (mui/mui-slide-transition "up"))

(fp/defsc MuiDialogs
  [this {:keys [value]}]
  {:initial-state (fn [_] {:value 30})
   :ident         (fn [] [::id "singleton"])
   :query         [:value]
   :use-hooks?    true}
  (let [^Object classes (use-styles)
        [open1 setOpen1] (hooks/use-state false)
        [open2 setOpen2] (hooks/use-state false)]
    (ui-theme-provider
      {:theme theme}
      (mui/mui-container
        {}
        (mui/mui-button {:onClick (fn [] (setOpen1 true))} "Simple dialog")
        (mui/mui-dialog
          {:open open1 :onClose (fn [] (setOpen1 false))}
          (mui/mui-dialog-title {} "Title")
          (mui/mui-dialog-content
            {}
            (mui/mui-dialog-content-text {} "Text"))
          (mui/mui-dialog-actions
            {}
            (mui/mui-button {:onClick (fn [] (setOpen1 false))} "Cancel")
            (mui/mui-button {:onClick (fn [] (setOpen1 false))} "Continue"))))
      (mui/mui-container
        {}
        (mui/mui-button {:onClick (fn [] (setOpen2 true))} "Fullscreen dialog")
        (mui/mui-dialog
          {:fullScreen          true :open open2 :onClose (fn [] (setOpen2 false))
           :TransitionComponent transition-up}
          (mui/mui-app-bar
            {:style {:position "relative"}}
            (mui/mui-toolbar
              {}
              (mui/mui-icon-button {:onClick (fn [] (setOpen2 false))} (icons/close))
              (mui/mui-typography {:variant "h6" :style {:flex 1 :marginLeft "20px"}} "Sound")
              (mui/mui-button {:autoFocus true :onClick (fn [] (setOpen2 false))} "Save")))
          (mui/mui-dialog-content
            {}
            (mui/mui-dialog-content-text {} "Text"))
          (mui/mui-dialog-actions
            {}
            (mui/mui-button {:onClick (fn [] (setOpen2 false))} "Cancel")
            (mui/mui-button {:onClick (fn [] (setOpen2 false))} "Continue")))))))

(comment
  (declare room-admin-access)
  (go
    (some-> )))

(ws/defcard mui-dialogs-card
  {::wsm/card-width  7
   ::wsm/card-height 10}
  (ct.fulcro/fulcro-card
    {::ct.fulcro/root       MuiDialogs
     ::ct.fulcro/wrap-root? true}))

(fp/defsc MuiSnackbars
  [this {:keys [value]}]
  {:initial-state (fn [_] {:value 30})
   :ident         (fn [] [::id "singleton"])
   :query         [:value]
   :use-hooks?    true}
  (let [^Object classes (use-styles)
        [open1 setOpen1] (hooks/use-state false)
        [open2 setOpen2] (hooks/use-state false)
        [open3 setOpen3] (hooks/use-state false)
        [open4 setOpen4] (hooks/use-state false)
        [open5 setOpen5] (hooks/use-state false)]
    (ui-theme-provider
      {:theme theme}
      (mui/mui-container
        {}
        (mui/mui-button {:onClick (fn [] (setOpen1 true))} "Simple snackbar")
        (mui/mui-button {:onClick (fn [] (setOpen2 true))} "Success snackbar")
        (mui/mui-button {:onClick (fn [] (setOpen3 true))} "Warning snackbar")
        (mui/mui-button {:onClick (fn [] (setOpen4 true))} "Info snackbar")
        (mui/mui-button {:onClick (fn [] (setOpen5 true))} "Error snackbar")
        (mui/mui-snackbar
          {:open    open1
           :onClose (fn [] (setOpen1 false))
           :message "Snackbar message"
           :action  (dom/div
                      {}
                      (mui/mui-button {:color   "secondary"
                                       :variant "contained"
                                       :onClick (fn [] (setOpen1 false))} "Cancel")
                      (mui/mui-button {:color   "primary"
                                       :variant "contained"
                                       :onClick (fn [] (setOpen1 false))} "Continue"))})
        (mui/mui-snackbar
          {:open         open2
           :onClose      (fn [] (setOpen2 false))
           :anchorOrigin {:vertical "top" :horizontal "left"}
           :autoHideDuration 5000}
          (mui/mui-alert
            {:elevation 6
             :variant   "filled"
             :severity  "success"}
            "Success message"))
        (mui/mui-snackbar
          {:open         open3
           :onClose      (fn [] (setOpen3 false))
           :anchorOrigin {:vertical "bottom" :horizontal "left"}
           :autoHideDuration 5000}
          (mui/mui-alert
            {:elevation 6
             :variant   "filled"
             :severity  "warning"}
            "Warning message"))
        (mui/mui-snackbar
          {:open         open4
           :onClose      (fn [] (setOpen4 false))
           :anchorOrigin {:vertical "bottom" :horizontal "right"}
           :autoHideDuration 5000}
          (mui/mui-alert
            {:elevation 6
             :variant   "filled"
             :severity  "info"}
            "Info message"))
        (mui/mui-snackbar
          {:open         open5
           :onClose      (fn [] (setOpen5 false))
           :anchorOrigin {:vertical "top" :horizontal "right"}
           :autoHideDuration 5000}
          (mui/mui-alert
            {:elevation 6
             :variant   "filled"
             :severity  "error"}
            "Error message"))))))

(ws/defcard mui-snackbars-card
  {::wsm/card-width  7
   ::wsm/card-height 10}
  (ct.fulcro/fulcro-card
    {::ct.fulcro/root       MuiSnackbars
     ::ct.fulcro/wrap-root? true}))

(fp/defsc MuiBackdrop
  [this {:keys [value]}]
  {:initial-state (fn [_] {:value 30})
   :ident         (fn [] [::id "singleton"])
   :query         [:value]
   :use-hooks?    true}
  (let [^Object classes (use-styles)
        [open setOpen] (hooks/use-state false)]
    (ui-theme-provider
      {:theme theme}
      (mui/mui-container
        {}
        (mui/mui-button {:onClick (fn [] (setOpen true))} "Show backdrop")
        (mui/mui-backdrop
          {:open open
           :onClick (fn [] (setOpen false))
           :className (.-backdrop classes)}
          (mui/mui-circular-progress
            {:color "inherit"}))))))

(ws/defcard mui-backdrop-card
  {::wsm/card-width  7
   ::wsm/card-height 10}
  (ct.fulcro/fulcro-card
    {::ct.fulcro/root       MuiBackdrop
     ::ct.fulcro/wrap-root? true}))