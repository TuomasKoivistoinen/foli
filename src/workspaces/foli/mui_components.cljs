(ns foli.mui-components
  (:require
    [com.fulcrologic.fulcro.algorithms.react-interop :as interop]
    ["@material-ui/core/Button" :default material-button]
    ["@material-ui/core/ButtonGroup" :default material-button-group]
    ["@material-ui/core/Container" :default material-container]
    ["@material-ui/core/Checkbox" :default material-checkbox]
    ["@material-ui/core/FormControlLabel" :default material-form-control-label]
    ["@material-ui/core/FormControl" :default material-form-control]
    ["@material-ui/core/Fab" :default material-fab]
    ["@date-io/moment" :as MomentUtils]
    ["@material-ui/pickers" :refer [MuiPickersUtilsProvider KeyboardTimePicker KeyboardDatePicker]]
    ["@material-ui/core/RadioGroup" :default material-radio-group]
    ["@material-ui/core/Radio" :default material-radio]
    ["@material-ui/lab/Autocomplete" :default material-auto-complete]
    ["@material-ui/core/TextField" :default material-text-field]
    ["@material-ui/core/Select" :default material-select]
    ["@material-ui/core/Menu" :default material-menu]
    ["@material-ui/core/MenuItem" :default material-menu-item]
    ["@material-ui/core/InputLabel" :default material-input-label]
    ["@material-ui/core/FormHelperText" :default material-form-helper-text]
    ["@material-ui/core/Slider" :default material-slider]
    ["@material-ui/core/Grid" :default material-grid]
    ["@material-ui/core/Typography" :default material-typography]
    ["@material-ui/core/Switch" :default material-switch]
    ["@material-ui/core/BottomNavigation" :default material-bottom-navigation]
    ["@material-ui/core/BottomNavigationAction" :default material-bottom-navigation-action]
    ["@material-ui/core/Breadcrumbs" :default material-breadcrumbs]
    ["@material-ui/core/Link" :default material-link]
    ["@material-ui/core/Drawer" :default material-drawer]
    ["@material-ui/core/List" :default material-list]
    ["@material-ui/core/Divider" :default material-divider]
    ["@material-ui/core/ListItem" :default material-list-item]
    ["@material-ui/core/ListItemIcon" :default material-list-item-icon]
    ["@material-ui/core/ListItemText" :default material-list-item-text]
    ["@material-ui/core/AppBar" :default material-app-bar]
    ["@material-ui/core/Toolbar" :default material-toolbar]
    ["@material-ui/core/Stepper" :default material-stepper]
    ["@material-ui/core/Step" :default material-step]
    ["@material-ui/core/StepLabel" :default material-step-label]
    ["@material-ui/core/Tabs" :default material-tabs]
    ["@material-ui/core/Tab" :default material-tab]
    ["@material-ui/core/IconButton" :default material-icon-button]
    ["@material-ui/core/Paper" :default material-paper]
    ["@material-ui/core/Card" :default material-card]
    ["@material-ui/core/CardActions" :default material-card-actions]
    ["@material-ui/core/CardContent" :default material-card-content]
    ["@material-ui/core/CardMedia" :default material-card-media]
    ["@material-ui/core/CardHeader" :default material-card-header]
    ["@material-ui/core/Collapse" :default material-collapse]
    ["@material-ui/core/Avatar" :default material-avatar]
    ["@material-ui/core/CircularProgress" :default material-circular-progress]
    ["@material-ui/core/LinearProgress" :default material-linear-progress]
    ["@material-ui/core/Dialog" :default material-dialog]
    ["@material-ui/core/DialogTitle" :default material-dialog-title]
    ["@material-ui/core/DialogActions" :default material-dialog-actions]
    ["@material-ui/core/DialogContent" :default material-dialog-content]
    ["@material-ui/core/DialogContentText" :default material-dialog-content-text]
    ["@material-ui/core/Slide" :default material-slide]
    ["@material-ui/core/Snackbar" :default material-snackbar]
    ["@material-ui/lab/Alert" :default material-alert]
    ["@material-ui/core/Backdrop" :default material-backdrop]
    ["@material-ui/lab/AvatarGroup" :default material-avatar-group]
    ["@material-ui/core/Badge" :default material-badge]
    ["@material-ui/core/Chip" :default material-chip]
    ["@material-ui/core/Icon" :default material-icon]
    ["@material-ui/core/ListSubheader" :default material-list-subheader]
    ["@material-ui/core/ListItemAvatar" :default material-list-item-avatar]
    ["@material-ui/core/ListItemSecondaryAction" :default material-list-item-secondary-action]
    ["@material-ui/core/Table" :default material-table]
    ["@material-ui/core/TableBody" :default material-table-body]
    ["@material-ui/core/TableCell" :default material-table-cell]
    ["@material-ui/core/TableContainer" :default material-table-container]
    ["@material-ui/core/TableHead" :default material-table-head]
    ["@material-ui/core/TableRow" :default material-table-row]
    ["@material-ui/core/TableSortLabel" :default material-table-sort-label]
    ["@material-ui/core/Tooltip" :default material-tooltip]))

(def mui-button (interop/react-factory material-button))
(def mui-button-group (interop/react-factory material-button-group))
(def mui-checkbox (interop/react-factory material-checkbox))
(def mui-container (interop/react-factory material-container))
(def mui-form-control-label (interop/react-factory material-form-control-label))
(def mui-form-control (interop/react-factory material-form-control))
(def mui-fab (interop/react-factory material-fab))
(def mui-datepicker-utils-provider (interop/react-factory MuiPickersUtilsProvider))
(def mui-date-picker (interop/react-factory KeyboardDatePicker))
(def mui-time-picker (interop/react-factory KeyboardTimePicker))
(def mui-radio-group (interop/react-factory material-radio-group))
(def mui-radio (interop/react-factory material-radio))
(def mui-autocomplete (interop/react-factory material-auto-complete))
(def mui-text-field (interop/react-factory material-text-field))
(def mui-select (interop/react-factory material-select))
(def mui-menu-item (interop/react-factory material-menu-item))
(def mui-menu (interop/react-factory material-menu))
(def mui-input-label (interop/react-factory material-input-label))
(def mui-form-helper-text (interop/react-factory material-form-helper-text))
(def mui-slider (interop/react-factory material-slider))
(def mui-grid (interop/react-factory material-grid))
(def mui-typography (interop/react-factory material-typography))
(def mui-switch (interop/react-factory material-switch))
(def mui-bottom-navigation (interop/react-factory material-bottom-navigation))
(def mui-bottom-navigation-action (interop/react-factory material-bottom-navigation-action))
(def mui-breadcrumbs (interop/react-factory material-breadcrumbs))
(def mui-link (interop/react-factory material-link))
(def mui-drawer (interop/react-factory material-drawer))
(def mui-list (interop/react-factory material-list))
(def mui-divider (interop/react-factory material-divider))
(def mui-list-item (interop/react-factory material-list-item))
(def mui-list-item-icon (interop/react-factory material-list-item-icon))
(def mui-list-item-text (interop/react-factory material-list-item-text))
(def mui-app-bar (interop/react-factory material-app-bar))
(def mui-toolbar (interop/react-factory material-toolbar))
(def mui-stepper (interop/react-factory material-stepper))
(def mui-step (interop/react-factory material-step))
(def mui-step-label (interop/react-factory material-step-label))
(def mui-tabs (interop/react-factory material-tabs))
(def mui-tab (interop/react-factory material-tab))
(def mui-icon-button (interop/react-factory material-icon-button))
(def mui-paper (interop/react-factory material-paper))
(def mui-card (interop/react-factory material-card))
(def mui-card-header (interop/react-factory material-card-header))
(def mui-card-media (interop/react-factory material-card-media))
(def mui-card-actions (interop/react-factory material-card-actions))
(def mui-card-content (interop/react-factory material-card-content))
(def mui-collapse (interop/react-factory material-collapse))
(def mui-avatar (interop/react-factory material-avatar))
(def mui-circular-progress (interop/react-factory material-circular-progress))
(def mui-linear-progress (interop/react-factory material-linear-progress))
(def mui-dialog (interop/react-factory material-dialog))
(def mui-dialog-title (interop/react-factory material-dialog-title))
(def mui-dialog-content (interop/react-factory material-dialog-content))
(def mui-dialog-content-text (interop/react-factory material-dialog-content-text))
(def mui-dialog-actions (interop/react-factory material-dialog-actions))
(def mui-snackbar (interop/react-factory material-snackbar))
(def mui-alert (interop/react-factory material-alert))
(def mui-backdrop (interop/react-factory material-backdrop))
(def mui-avatar-group (interop/react-factory material-avatar-group))
(def mui-badge (interop/react-factory material-badge))
(def mui-chip (interop/react-factory material-chip))
(def mui-icon (interop/react-factory material-icon))
(def mui-list-subheader (interop/react-factory material-list-subheader))
(def mui-list-item-avatar (interop/react-factory material-list-item-avatar))
(def mui-list-item-secondary-action (interop/react-factory material-list-item-secondary-action))
(def mui-table (interop/react-factory material-table))
(def mui-table-body (interop/react-factory material-table-body))
(def mui-table-cell (interop/react-factory material-table-cell))
(def mui-table-container (interop/react-factory material-table-container))
(def mui-table-head (interop/react-factory material-table-head))
(def mui-table-row (interop/react-factory material-table-row))
(def mui-table-sort-label (interop/react-factory material-table-sort-label))
(def mui-tooltip (interop/react-factory material-tooltip))

(defn mui-slide-transition [direction]
  (js/React.forwardRef (fn [props ref]
                         (->> (js->clj props)
                              (merge {:ref ref :direction direction})
                              (clj->js)
                              (js/React.createElement material-slide)))))

(defn mui-default-datepicker-utils-provider [& children]
  (mui-datepicker-utils-provider {:utils MomentUtils} children))