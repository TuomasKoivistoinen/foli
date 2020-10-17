(ns foli.inputs
  (:require [com.fulcrologic.fulcro.components :as fp]
            [com.fulcrologic.fulcro.mutations :as fm]
            [com.fulcrologic.fulcro.react.hooks :as hooks]
            [nubank.workspaces.core :as ws]
            [nubank.workspaces.model :as wsm]
            [nubank.workspaces.card-types.fulcro3 :as ct.fulcro]
            [foli.styles :refer [ui-theme-provider use-styles theme]]
            [foli.mui-components :as mui]
            [foli.ui-icons :as icons]))

(def buttons [{:color "primary" :variant "contained"}
              {:color "secondary" :variant "contained"}
              {:color "secondary" :variant "contained" :disabled true}
              {:color "primary"}
              {:color "secondary"}
              {:disabled true}
              {:color "primary" :variant "outlined"}
              {:color "secondary" :variant "outlined"}
              {:color "secondary" :variant "outlined" :disabled true}])

(fp/defsc MuiButtons
          [this {:keys [counter]}]
          {:initial-state (fn [_] {:counter 0})
           :ident         (fn [] [::id "singleton"])
           :query         [:counter]
           :use-hooks?    true}
          (let [text (str "counter " counter "")
                ^Object classes (use-styles)]
               (ui-theme-provider
                 {:theme theme}
                 (mui/mui-container
                   {}
                   (map-indexed (fn [idx props]
                                    (mui/mui-button
                                      (merge
                                        {:className (^js .-margin classes)
                                         :onClick   #(fm/set-value! this :counter (inc counter))
                                         :key       idx}
                                        props)
                                      text))
                                buttons)
                   (mui/mui-button
                     {:key       100
                      :className (^js .-customButton classes)}
                     "custom")))))

(def button-groups [{:color   "primary"
                     :variant "contained"}
                    {:color "primary"}
                    {:color   "primary"
                     :variant "text"}
                    {:color   "secondary"
                     :variant "contained"}
                    {:color "secondary"}
                    {:color   "secondary"
                     :variant "text"}])

(fp/defsc MuiButtonGroups
          [this {:keys [counter]}]
          {:initial-state (fn [_] {:counter 0})
           :ident         (fn [] [::id "singleton"])
           :query         [:counter]
           :use-hooks?    true}
          (let [text (str "counter " counter "")
                classes (use-styles)]
               (ui-theme-provider
                 {:theme theme}
                 (mui/mui-container
                   {}
                   (map-indexed (fn [idx props]
                                    (mui/mui-button-group
                                      (merge
                                        {:className (^js .-margin classes)
                                         :onClick   #(fm/set-value! this :counter (inc counter))
                                         :key       idx}
                                        props)
                                      (mui/mui-button {} text)
                                      (mui/mui-button {} text)
                                      (mui/mui-button {} text)))
                                button-groups)))))

(def floating-action-buttons [{:color "primary"
                               :icon  (icons/add)}
                              {:color "secondary"
                               :icon  (icons/edit)}
                              {:color   "primary"
                               :variant "extended"
                               :icon    (icons/navigation)
                               :text    "Navigate"}
                              {:disabled true
                               :icon     (icons/delete)}])

(fp/defsc MuiFloatingActionButton
          [this {:keys [counter]}]
          {:initial-state (fn [_] {:counter 0})
           :ident         (fn [] [::id "singleton"])
           :query         [:counter]
           :use-hooks?    true}
          (let [classes (use-styles)]
               (ui-theme-provider
                 {:theme theme}
                 (mui/mui-container
                   {}
                   (map-indexed (fn [idx props]
                                    (mui/mui-fab
                                      (merge
                                        {:className (^js .-margin classes)
                                         :key       idx}
                                        props)
                                      (:icon props)
                                      (:text props)))
                                floating-action-buttons)))))


(ws/defcard mui-buttons-card
            {::wsm/card-width  5
             ::wsm/card-height 7}
            (ct.fulcro/fulcro-card
              {::ct.fulcro/root       MuiButtons
               ::ct.fulcro/wrap-root? true}))

(ws/defcard mui-button-groups-card
            {::wsm/card-width  5
             ::wsm/card-height 10}
            (ct.fulcro/fulcro-card
              {::ct.fulcro/root       MuiButtonGroups
               ::ct.fulcro/wrap-root? true}))

(ws/defcard mui-floating-action-button-card
            {::wsm/card-width  5
             ::wsm/card-height 7}
            (ct.fulcro/fulcro-card
              {::ct.fulcro/root       MuiFloatingActionButton
               ::ct.fulcro/wrap-root? true}))

(def checkboxes [{:label "primary" :color "primary"}
                 {:label "secondary" :color "secondary"}
                 {:label "disabled" :color "primary" :disabled true}
                 {:label "custom icon" :icon (icons/favorite-border) :checkedIcon (icons/favorite)}])

(fp/defsc MuiCheckboxes
          [this {:keys [counter]}]
          {:initial-state (fn [_] {:counter 0})
           :ident         (fn [] [::id "singleton"])
           :query         [:counter]
           :use-hooks?    true}
          (let [text (str "counter " counter "")
                classes (use-styles)]
               (ui-theme-provider
                 {:theme theme}
                 (mui/mui-container
                   {}
                   (map-indexed (fn [idx {:keys [label] :as props}]
                                    (mui/mui-form-control-label
                                      {:key     idx
                                       :label   label
                                       :control (mui/mui-checkbox
                                                  (merge {:className (^js .-margin classes)}
                                                         props))}))
                                checkboxes)))))

(ws/defcard mui-checkboxes-card
            {::wsm/card-width  5
             ::wsm/card-height 7}
            (ct.fulcro/fulcro-card
              {::ct.fulcro/root       MuiCheckboxes
               ::ct.fulcro/wrap-root? true}))

(def radios [{:label "primary" :color "primary"}
             {:label "secondary" :color "secondary"}
             {:label "disabled" :color "primary" :disabled true}
             {:label "custom" :icon (icons/favorite-border) :checkedIcon (icons/favorite)}])

(fp/defsc MuiRadio
          [this {:keys [:selection]}]
          {:initial-state (fn [_] {:selection "primary"})
           :ident         (fn [] [::id "singleton"])
           :query         [:selection]
           :use-hooks?    true}
          (let [classes (use-styles)]
               (ui-theme-provider
                 {:theme theme}
                 (mui/mui-container
                   {}
                   (mui/mui-radio-group
                     {:onChange (fn [evt] (fm/set-string! this :selection :event evt))}
                     (map-indexed (fn [idx {:keys [label] :as props}]
                                      (mui/mui-form-control-label
                                        {:key     idx
                                         :label   label
                                         :value   label
                                         :control (mui/mui-radio
                                                    (merge {:className (^js .-margin classes)}
                                                           props))}))
                                  radios))))))

(ws/defcard mui-radio-card
            {::wsm/card-width  5
             ::wsm/card-height 8}
            (ct.fulcro/fulcro-card
              {::ct.fulcro/root       MuiRadio
               ::ct.fulcro/wrap-root? true}))

(fp/defsc MuiDatePickers
          [this {:keys [counter]}]
          {:initial-state (fn [_] {:counter 0})
           :ident         (fn [] [::id "singleton"])
           :query         [:counter]
           :use-hooks?    true}
          (let [classes (use-styles)]
               (ui-theme-provider
                 {:theme theme}
                 (mui/mui-container
                   {}
                   (mui/mui-default-datepicker-utils-provider
                     (mui/mui-date-picker
                       {:className (^js .-margin classes)
                        :label     "Date picker inline"
                        :key       1
                        :onChange  (fn [d] (println d))
                        :variant   "inline"})
                     (mui/mui-date-picker
                       {:className (^js .-margin classes)
                        :label     "Date picker dialog"
                        :key       2
                        :onChange  (fn [d] (println d))
                        :variant   "dialog"})
                     (mui/mui-time-picker
                       {:className (^js .-margin classes)
                        :label     "Time picker dialog"
                        :key       3
                        :onChange  (fn [d] (println d))}))))))

(ws/defcard mui-date-pickers-card
            {::wsm/card-width  5
             ::wsm/card-height 7}
            (ct.fulcro/fulcro-card
              {::ct.fulcro/root       MuiDatePickers
               ::ct.fulcro/wrap-root? true}))

(def top-100-films [{:title "The Shawshank Redemption" :year 1994}
                    {:title "The Godfather" :year 1972}
                    {:title "The Godfather: Part II" :year 1974}
                    {:title "The Dark Knight" :year 2008}
                    {:title "12 Angry Men" :year 1957}
                    {:title "Schindler's List" :year 1993}
                    {:title "Pulp Fiction" :year 1994}
                    {:title "The Lord of the Rings: The Return of the King" :year 2003}
                    {:title "The Good, the Bad and the Ugly" :year 1966}
                    {:title "Fight Club" :year 1999}
                    {:title "The Lord of the Rings: The Fellowship of the Ring" :year 2001}
                    {:title "Star Wars: Episode V - The Empire Strikes Back" :year 1980}
                    {:title "Forrest Gump" :year 1994}
                    {:title "Inception" :year 2010}
                    {:title "The Lord of the Rings: The Two Towers" :year 2002}
                    {:title "One Flew Over the Cuckoo's Nest" :year 1975}
                    {:title "Goodfellas" :year 1990}
                    {:title "The Matrix" :year 1999}
                    {:title "Seven Samurai" :year 1954}
                    {:title "Star Wars: Episode IV - A New Hope" :year 1977}
                    {:title "City of God" :year 2002}
                    {:title "Se7en" :year 1995}
                    {:title "The Silence of the Lambs" :year 1991}
                    {:title "It's a Wonderful Life" :year 1946}
                    {:title "Life Is Beautiful" :year 1997}
                    {:title "The Usual Suspects" :year 1995}
                    {:title "Léon: The Professional" :year 1994}
                    {:title "Spirited Away" :year 2001}
                    {:title "Saving Private Ryan" :year 1998}
                    {:title "Once Upon a Time in the West" :year 1968}
                    {:title "American History X" :year 1998}
                    {:title "Interstellar" :year 2014}
                    {:title "Casablanca" :year 1942}
                    {:title "City Lights" :year 1931}
                    {:title "Psycho" :year 1960}
                    {:title "The Green Mile" :year 1999}
                    {:title "The Intouchables" :year 2011}
                    {:title "Modern Times" :year 1936}
                    {:title "Raiders of the Lost Ark" :year 1981}
                    {:title "Rear Window" :year 1954}
                    {:title "The Pianist" :year 2002}
                    {:title "The Departed" :year 2006}
                    {:title "Terminator 2: Judgment Day" :year 1991}
                    {:title "Back to the Future" :year 1985}
                    {:title "Whiplash" :year 2014}
                    {:title "Gladiator" :year 2000}
                    {:title "Memento" :year 2000}
                    {:title "The Prestige" :year 2006}
                    {:title "The Lion King" :year 1994}
                    {:title "Apocalypse Now" :year 1979}
                    {:title "Alien" :year 1979}
                    {:title "Sunset Boulevard" :year 1950}
                    {:title "Dr. Strangelove or: How I Learned to Stop Worrying and Love the Bomb" :year 1964}
                    {:title "The Great Dictator" :year 1940}
                    {:title "Cinema Paradiso" :year 1988}
                    {:title "The Lives of Others" :year 2006}
                    {:title "Grave of the Fireflies" :year 1988}
                    {:title "Paths of Glory" :year 1957}
                    {:title "Django Unchained" :year 2012}
                    {:title "The Shining" :year 1980}
                    {:title "WALL·E" :year 2008}
                    {:title "American Beauty" :year 1999}
                    {:title "The Dark Knight Rises" :year 2012}
                    {:title "Princess Mononoke" :year 1997}
                    {:title "Aliens" :year 1986}
                    {:title "Oldboy" :year 2003}
                    {:title "Once Upon a Time in America" :year 1984}
                    {:title "Witness for the Prosecution" :year 1957}
                    {:title "Das Boot" :year 1981}
                    {:title "Citizen Kane" :year 1941}
                    {:title "North by Northwest" :year 1959}
                    {:title "Vertigo" :year 1958}
                    {:title "Star Wars: Episode VI - Return of the Jedi" :year 1983}
                    {:title "Reservoir Dogs" :year 1992}
                    {:title "Braveheart" :year 1995}
                    {:title "M" :year 1931}
                    {:title "Requiem for a Dream" :year 2000}
                    {:title "Amélie" :year 2001}
                    {:title "A Clockwork Orange" :year 1971}
                    {:title "Like Stars on Earth" :year 2007}
                    {:title "Taxi Driver" :year 1976}
                    {:title "Lawrence of Arabia" :year 1962}
                    {:title "Double Indemnity" :year 1944}
                    {:title "Eternal Sunshine of the Spotless Mind" :year 2004}
                    {:title "Amadeus" :year 1984}
                    {:title "To Kill a Mockingbird" :year 1962}
                    {:title "Toy Story 3" :year 2010}
                    {:title "Logan" :year 2017}
                    {:title "Full Metal Jacket" :year 1987}
                    {:title "Dangal" :year 2016}
                    {:title "The Sting" :year 1973}
                    {:title "2001: A Space Odyssey" :year 1968}
                    {:title "Singin' in the Rain" :year 1952}
                    {:title "Toy Story" :year 1995}
                    {:title "Bicycle Thieves" :year 1948}
                    {:title "The Kid" :year 1921}
                    {:title "Inglourious Basterds" :year 2009}
                    {:title "Snatch" :year 2000}
                    {:title "3 Idiots" :year 2009}
                    {:title "Monty Python and the Holy Grail" :year 1975}])

(fp/defsc MuiSelects
          [this {:keys [selection]}]
          {:initial-state (fn [_] {:selection ""})
           :ident         (fn [] [::id "singleton"])
           :query         [:selection]
           :use-hooks?    true}
          (let [classes (use-styles)]
               (println (^js .-formControl classes))
               (ui-theme-provider
                 {:theme theme}
                 (mui/mui-container
                   {}
                   (mui/mui-form-control
                     {:key       1
                      :className (^js .-formControl classes)}
                     (mui/mui-input-label {} "Choose a movie")
                     (mui/mui-select
                       {:value    selection
                        :onChange (fn [evt] (fm/set-string! this :selection :event evt))}
                       (map-indexed (fn [idx {:keys [title]}]
                                        (mui/mui-menu-item
                                          {:value (str idx)}
                                          title))
                                    top-100-films))
                     (mui/mui-form-helper-text {} "Optional helper text"))
                   (mui/mui-form-control
                     {:key       2
                      :className (^js .-formControl classes)
                      :error     true}
                     (mui/mui-input-label {} "Choose a movie")
                     (mui/mui-select
                       {:value    selection
                        :onChange (fn [evt] (fm/set-string! this :selection :event evt))}
                       (map-indexed (fn [idx {:keys [title]}]
                                        (mui/mui-menu-item
                                          {:value (str idx)}
                                          title))
                                    top-100-films))
                     (mui/mui-form-helper-text {} "Optional error message"))))))

(ws/defcard mui-selects-card
            {::wsm/card-width  7
             ::wsm/card-height 7}
            (ct.fulcro/fulcro-card
              {::ct.fulcro/root       MuiSelects
               ::ct.fulcro/wrap-root? true}))

(fp/defsc MuiSliders
          [this {:keys [value]}]
          {:initial-state (fn [_] {:value 30})
           :ident         (fn [] [::id "singleton"])
           :query         [:value]
           :use-hooks?    true}
          (let [[value setValue] (hooks/use-state 30)
                [value2 setValue2] (hooks/use-state [10 20])
                [value3 setValue3] (hooks/use-state 3)
                [value4 setValue4] (hooks/use-state [10 20])
                [value5 setValue5] (hooks/use-state 37)]
               (ui-theme-provider
                 {:theme theme}
                 (mui/mui-container
                   {:style {:width 400}}
                   (mui/mui-typography
                     {:id           "continuous-slider"
                      :gutterBottom true}
                     (str "Continuous slider " value " %"))
                   (mui/mui-slider
                     {:aria-labelledby   "continuous-slider"
                      :value             value
                      :onChange          (fn [_ new-value] (setValue new-value))
                      :valueLabelDisplay "auto"})
                   (mui/mui-typography
                     {:id           "discrete-slider"
                      :gutterBottom true}
                     (str "Discrete slider " value3))
                   (mui/mui-slider
                     {:aria-labelledby   "discrete-slider"
                      :value             value3
                      :onChange          (fn [_ new-value] (setValue3 new-value))
                      :step              10
                      :marks             true
                      :min               0
                      :max               100
                      :valueLabelDisplay "auto"
                      :color             "secondary"})
                   (mui/mui-typography
                     {:id           "discrete-slider-range"
                      :gutterBottom true}
                     (str "Range slider " (first value4) " - " (last value4)))
                   (mui/mui-slider
                     {:aria-labelledby   "discrete-slider-range"
                      :value             value4
                      :onChange          (fn [_ new-value] (setValue4 new-value))
                      :step              10
                      :marks             true
                      :min               0
                      :max               100
                      :valueLabelDisplay "auto"})
                   (mui/mui-typography
                     {:id           "slider-custom-marks"
                      :gutterBottom true}
                     (str "Custom discrete steps " value5 "°C"))
                   (mui/mui-slider
                     {:aria-labelledby   "slider-custom-marks"
                      :value             value5
                      :onChange          (fn [_ new-value] (setValue5 new-value))
                      :step              nil
                      :marks             [{:value 0 :label "0°C"}
                                          {:value 20 :label "20°C"}
                                          {:value 37 :label "37°C"}
                                          {:value 100 :label "100°C"}]
                      :min               0
                      :max               100
                      :color             "secondary"
                      :valueLabelDisplay "auto"})))))

(ws/defcard mui-sliders-card
            {::wsm/card-width  5
             ::wsm/card-height 14}
            (ct.fulcro/fulcro-card
              {::ct.fulcro/root       MuiSliders
               ::ct.fulcro/wrap-root? true}))

(fp/defsc MuiSwitches
          [this {:keys [value]}]
          {:initial-state (fn [_] {:value 30})
           :ident         (fn [] [::id "singleton"])
           :query         [:value]
           :use-hooks?    true}
          (let [[value1 setValue1] (hooks/use-state true)
                [value2 setValue2] (hooks/use-state true)]
               (ui-theme-provider
                 {:theme theme}
                 (mui/mui-container
                   {}
                   (mui/mui-form-control-label
                     {:key     1
                      :control (mui/mui-switch
                                 {:color    "primary"
                                  :checked  value1
                                  :onChange (fn [] (setValue1 (not value1)))})
                      :label   "Primary"})
                   (mui/mui-form-control-label
                     {:key     2
                      :control (mui/mui-switch
                                 {:color    "secondary"
                                  :checked  value2
                                  :onChange (fn [] (setValue2 (not value2)))})
                      :label   "Secondary"})
                   (mui/mui-form-control-label
                     {:key     3
                      :control (mui/mui-switch
                                 {:checked  false
                                  :disabled true})
                      :label   "Disabled"})))))

(ws/defcard mui-switches-card
            {::wsm/card-width  5
             ::wsm/card-height 7}
            (ct.fulcro/fulcro-card
              {::ct.fulcro/root       MuiSwitches
               ::ct.fulcro/wrap-root? true}))

(fp/defsc MuiTextFields
          [this {:keys [value]}]
          {:initial-state (fn [_] {:value 30})
           :ident         (fn [] [::id "singleton"])
           :query         [:value]
           :use-hooks?    true}
          (let [^Object classes (use-styles)
                margin (.-margin classes)]
               (ui-theme-provider
                 {:theme theme}
                 (mui/mui-container
                   {}
                   (mui/mui-text-field
                     {:label     "Standard"
                      :className margin})
                   (mui/mui-text-field
                     {:label     "Filled"
                      :variant   "filled"
                      :className margin})
                   (mui/mui-text-field
                     {:label     "Outlined"
                      :variant   "outlined"
                      :className margin}))
                 (mui/mui-container
                   {}
                   (mui/mui-text-field
                     {:id         "error-standard"
                      :label      "Standard error"
                      :className  margin
                      :error      true
                      :helperText "Error text"})
                   (mui/mui-text-field
                     {:id         "error-filled"
                      :label      "Filled error"
                      :variant    "filled"
                      :className  margin
                      :error      true
                      :helperText "Error text"})
                   (mui/mui-text-field
                     {:id         "error-filled"
                      :label      "Outlined error"
                      :variant    "outlined"
                      :className  margin
                      :error      true
                      :helperText "Error text"})))))

(ws/defcard mui-text-fields-card
            {::wsm/card-width  7
             ::wsm/card-height 10}
            (ct.fulcro/fulcro-card
              {::ct.fulcro/root       MuiTextFields
               ::ct.fulcro/wrap-root? true}))