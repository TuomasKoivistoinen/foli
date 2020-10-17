(ns foli.data-display
  (:require [com.fulcrologic.fulcro.components :as fp]
            [com.fulcrologic.fulcro.mutations :as fm]
            [com.fulcrologic.fulcro.react.hooks :as hooks]
            [com.fulcrologic.fulcro.application :as app]
            [nubank.workspaces.core :as ws]
            [nubank.workspaces.model :as wsm]
            [nubank.workspaces.card-types.fulcro3 :as ct.fulcro]
            [foli.styles :refer [ui-theme-provider make-styles theme]]
            [foli.mui-components :as mui]
            [foli.ui-icons :as icons]
            [com.fulcrologic.fulcro.dom :as dom]
            [cljs.core.async :as async]
            ["@material-ui/core/colors" :refer [deepPurple pink]]
            ["@material-ui/core/Paper" :default material-paper]
            ["@material-ui/core/styles" :refer [lighten]]
            [com.fulcrologic.fulcro.components :as comp]
            [fulcro.client.mutations :as m]))

(def use-styles
  (make-styles (fn [theme]
                 (let [spacing (get theme "spacing")]
                   {:root               {:display "flex"
                                         "& > *"  {:margin (spacing 1)}}
                    :chips              {:display        "flex"
                                         :justifyContent "center"
                                         :flexWrap       "wrap"
                                         "& > *"         {:margin (spacing 0.5)}}
                    :dividers           {:width           360
                                         :backgroundColor (get-in theme ["palette" "background" "paper"])}
                    :lists              {:width           280
                                         :backgroundColor (get-in theme ["palette" "background" "paper"])}
                    :tables             {:minWidth 750}
                    :selectTable        {:width "100%"}
                    :tablePaper         {:width        "100%"
                                         :marginBottom (spacing 2)}
                    :nested             {:paddingLeft (spacing 4)}
                    :margin             {"& > *" {:margin (spacing 3)}}
                    :purple             {:color           ((get-in theme ["palette" "getContrastText"]) (aget deepPurple 500))
                                         :backgroundColor (aget deepPurple 500)}
                    :pink               {:color           ((get-in theme ["palette" "getContrastText"]) (aget pink 500))
                                         :backgroundColor (aget pink 500)}
                    :smallBadge         {:width  22
                                         :height 22
                                         :border (str "2px solid " (get-in theme ["palette" "background" "paper"]))}
                    :styledBadge        {:backgroundColor "#44b700"
                                         :color           "#44b700"
                                         :boxShadow       (str "0 0 0 2px " (get-in theme ["palette" "background" "paper"]))
                                         "&::after"       {:position     "absolute"
                                                           :top          -1
                                                           :left         -1
                                                           :width        "100%"
                                                           :height       "100%"
                                                           :borderRadius "50%"
                                                           :animation    "$ripple 1.2s infinite ease-in-out"
                                                           :border       "1px solid currentColor"
                                                           :content      "''"}}
                    "@keyframes ripple" {"0%"   {:transform "scale(.8)"
                                                 :opacity   1}
                                         "100%" {:transform "scale(2.4)"
                                                 :opacity   0}}}))))

(fp/defsc MuiAvatars
  [this {:keys [value]}]
  {:initial-state (fn [_] {:value 30})
   :ident         (fn [] [::id "singleton"])
   :query         [:value]
   :use-hooks?    true}
  (let [classes (js->clj (use-styles))]
    (ui-theme-provider
      {:theme theme}
      (mui/mui-container
        {:className (classes "root")}
        (mui/mui-avatar
          {:alt "Elon Musk"
           :src "https://www.biography.com/.image/ar_1:1%2Cc_fill%2Ccs_srgb%2Cg_face%2Cq_auto:good%2Cw_300/MTY2MzU3Nzk2OTM2MjMwNTkx/elon_musk_royal_society.jpg"})
        (mui/mui-avatar
          {:alt "Nikola Tesla"
           :src "data:image/jpeg;base64,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"})
        (mui/mui-avatar
          {:alt "Elon Musk"
           :src "Broken image"} "M")
        (mui/mui-avatar
          {:alt     "Elon Musk"
           :src     "Broken image"
           :variant "rounded"})
        (mui/mui-avatar
          {:alt     "Elon Musk"
           :variant "square"})
        (mui/mui-avatar
          {:alt       "Elon Musk"
           :className (classes "purple")} "E")
        (mui/mui-avatar
          {:alt "Favorite Icon" :className (classes "pink")} (icons/favorite)))
      (mui/mui-container
        {:className (classes "root")}
        (mui/mui-avatar-group
          {:max 5}
          (mui/mui-avatar
            {:alt "Elon Musk"
             :src "https://www.biography.com/.image/ar_1:1%2Cc_fill%2Ccs_srgb%2Cg_face%2Cq_auto:good%2Cw_300/MTY2MzU3Nzk2OTM2MjMwNTkx/elon_musk_royal_society.jpg"})
          (mui/mui-avatar
            {:alt "Nikola Tesla"
             :src "data:image/jpeg;base64,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"})
          (mui/mui-avatar
            {:alt "Elon Musk"
             :src "Broken image"} "M")
          (mui/mui-avatar
            {:alt     "Elon Musk"
             :src     "Broken image"
             :variant "rounded"})
          (mui/mui-avatar
            {:alt     "Elon Musk"
             :variant "square"})
          (mui/mui-avatar
            {:alt       "Elon Musk"
             :className (classes "purple")} "E")
          (mui/mui-avatar
            {:alt "Favorite Icon" :className (classes "pink")} (icons/favorite))))
      (mui/mui-container
        {:className (classes "root")}
        (mui/mui-badge
          {:overlap      "circle"
           :anchorOrigin {:vertical "bottom" :horizontal "right"}
           :badgeContent (mui/mui-avatar
                           {:src       "https://www.biography.com/.image/ar_1:1%2Cc_fill%2Ccs_srgb%2Cg_face%2Cq_auto:good%2Cw_300/MTY2MzU3Nzk2OTM2MjMwNTkx/elon_musk_royal_society.jpg"
                            :alt       "Elon Musk"
                            :className (classes "smallBadge")})}
          (mui/mui-avatar
            {:src "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxMTEhUTExMWFhUXFxoaGBcXGBcdGBcXHRUaHRoYGxcaHSggGB0lHRgWITEhJSkrLi4uGB8zODMtNygtLisBCgoKDg0OGhAQGi0lHyUtLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLf/AABEIAQUAwQMBIgACEQEDEQH/xAAcAAABBQEBAQAAAAAAAAAAAAADAAECBAUGBwj/xABBEAABAwIDBQYEBAQFAwUBAAABAAIRAyEEMVEFEkFhcQYTgZGh8CKxwdEHMuHxFEJSciMzYoKSJbKzJENTotIW/8QAGQEAAwEBAQAAAAAAAAAAAAAAAAEDAgQF/8QAJREBAQACAgICAgEFAAAAAAAAAAECESExAxITQTJRgQQiQmFx/9oADAMBAAIRAxEAPwD1ElNvKAemDl5u3T6jbyjvIRKSey0LKcFCBUgjY0IlKHKdAElPKGCknsCEqJKZKEGYlPKUJBqYNvkKYqhR3VFwRuhYDlJVmhEa5OUDSmJUQ5MSmEoUJSlRQEkp/dQcUnFLYVe96JIe+ku1zjiEkzQmK8x1HJTJik3omOuRE6QSCembTwnhMAnCZHhPCrY3aNKi3eq1GU26vcGj1K5jHfiPgmgilU7x3DdGZ5TG8mcxt6digV8bSZ+d7W9SAvIcZ22rvd/ifxDW8A0btue9Tj18V0eysa2qxrjvkcZBaRzLRYjmJWLlZ9KfHqcu4p7UoOEtq0yMrOGeis06zTk5p6EH5LkqmAZUaXsAdqBYkafabfNYL8VQoSQ/hMPLmkEf6wfR3/JHvYXpvp6cUwC8LZ+J+Lp1pG66mDHdmSCOTpJB5gkdV6n2X7YYfGgBjgyrEmk4je5kEWcOnoq/9GXjsdElCSSGCShOExQDgJiE4SF0wgmJUnBRSoVd080kL+IHv9067NxHVFDk+8htKkvNjoPKk1MAnCZVNKo4NBcTAAkk5AdUgvPPxZ7SimwYJn56gBqRmKc2aI4uPoCtwYY+1028f2taX7mHh2tRwPdjxH5vBVNpdqX08O+pvgxIa5ogE8pzhcns/aQc1lNzN2mCG/HMGP5WtHxPP+1Z3b3am/WZSNNwps+I71t4AflDcgMh4qU3atMJvTIds6ri3mtXe+4kAkufByJ0BzgAWvYXUsV3lJu5S71upZusBA/sMkcyVawdSq1gqHe+MzDYk872jr5KD6RfcNw4cbwTXqVeheJgrcy3/CnTFo7Vqs/O+o5v9LnG3gSV3HZ7b1Lu+Ae24ibjiIPqFwO0aNRv52uGlnbvUFwnzVTC4hzCCDBBn31EhUvimU3Ozuunqj+19OhUuT8QBjOAciP6hOYXM9re1LcRLWtYQbuJ4kcxeediuPxGIc6N4kwIHRBCeHg1zaxuS8JMgzFlc2RtB+GrMrUjD2GRodQeRFlWGVyfqob3NXvI6fTfZTtHTx1AVadiLPac2ui459VsyvG/wPqA1awvO6DmYPUTB8V7F79Vz2aukM5JeDynJUQnCTKSTEykwJg6i4KaaEwo7vJJFj3ZJdmkAmqQKGBqpDovMjqThMEgnBWoxTPeGgk2AuvnXtFtI18bXq7wHxkAnRvwiLaBfQ2NbNN8CSWn5L5ix0d48tyLibiMzlHLJUwm6t4fto4fHbhYXlzibwDcAmwA56fou/xPY+pi3Uagp1GtgB2/uyW2OgPmFq/hJ2Wa6k3FVqUOJlhcBcZNIGgbEdSvVNwI+PfPReTy6vDmsP2UohjWlo+EQOSBW7EYc5NIOosfRdchuT+PFH3rz7aXYCi8Rfzn0XFba/DRzRNI3Xt1VU6tMFZ9ddKY+TKPm7H9mcRSs+mRzFwfK4WWzCOmI9F9JYzCtdmAVgbU2KwMcQIkG0mPLJP5MopPJL3Hg+JaQ6HWhMRa3pmtPb9OKhj3dZgarYZe2MqlnLc7Gdo34GuajRLXCHNIzEzx5r6Owtdr2Ne0y1zQ4EcQRK+U3tOa+lOxbCzA4ZjsxSbOYi2UG4WPJOdo+SN5OUwKcrCRAojCggIzUQHSKcJJhTtofJJS80y7doaABUgUFpUgV5UdQxThDlTaVqM1MLz/AB/4YMq4wVmuHdGrv1KUXINyAdCeHMr0CVLD1PjA1BI8CFSDHKzpoUWwAAIAgWRSUzFJWiRihuUnOQa1cNFzCVMOoVTrJn7Wo8Xt8woHH0j/ADt81i2VrVVahWbtb/LcNQtCs9pNigVIIIPFYbjwftFS/wAQk6nP5LFIOi7vtpgO7qkxLTwPDQhcZVwvGfh9c9PBa8OXGq6+5wph5BXuv4TU3uw7q1SoahqOEEkmIEbt9DK8OeAF73+FFJrdnU4MklxdPAk9NIVPJq6R8m5HYhOUwSKmgdqM1AaUZqcFTSUSU0oCvvHVJP3fuEl1oqLU6GHp95eY6RQVNrkJrkQOWoVEBQan+bScP6iD0LHH5gIjVHEU94RJFxcZrTMLaXarD0JD3gECTcZXvnyNlV2T20w2JfuUnydNVwe0diuxGIrU6f5mh3xOBg2sJsSSYEAjKSeCwez3ZfGNxdN7aDmMbVZ8RJaYDhMkwHTcRCJnlVZ48dPadp4h7Wkhec9o8TW3XOdWLRoMwPovTdqH4YXHY3s8K5IcSAdI+ohLyS7Z8djzLBHFVy5wDnNaC65Jdui8wCLmFsbF7QtbvB9N7mtO7vyYndmIcd4mOS9C2bsBlCCwOyucyeuqI3YlME93Sa2czuifkl6qXySsfZ1VtVofTe6OF8uS1sO8xBR6Gym0xZo+6hUsnJpje3M9ttnGpSL2j4m3t65ryloc94pwBJzA4Tde54qC0jULzbB9nicaZHwiSbWOWqW5Kt48tQuzHZSl3grVoLGuhoOTndOML0XaD+5YKlOBuiYgXHELCOMoisKLmkBklhGTpsSea0Ns1N+k1rT+f4B1S9rZzU8uby7LC1d5jXagH0UnIOBp7lNrdAAjAqqJ2hHaEFpRA5ahVIqMp5USigHdd7BTqU9Uy6k2cE9kNqlK810DNUgggqbCmVFUgoNU2rUZXGYZubQJOalSwt943IUsPUsigq0kZ3VHamcKpQKu7QZdZlb4RMrGXbUajDKK42VDCVw5oKnVrLXsWkcS9ZOIcrNeqVQrPWLW5AaglCqFjGl7oEZm0orlTxDg57aRuC0nqZED5qbbldpOdiINGi/fc4gED4QJ/q4Ltuz+yiO6D793c/3HIfNWcPhXEBrfh6RAW3hcOGN3R4nU6rWGH2WefGk91IBOoyqJw4CnKG1ECIVKUjknIUZRQDI0KdPvDT35J1fbDNanhRBUguGLkAptCaOacJgQIg8UJpUw5OUrBqL4Kugki2azwq22Nrd0wXguBAJ4GLLcy1OWdbc92x7Xfw+81zXb0WLYLZ/uXney+19Z73Fznw48XEgWyANgemq2tv4HEVYpfme6PisGxM3GvGeZWFtzsfiKZG65pECQDBH2WJZl26cJjJqvWNhbQp92BvCwHELTfUBEgyvFsHT3QAcQyRmA4k+V7ru+zQdDXb7iCIIcCD5FEy+k8sNcuhrPVR6PVKrPKbIbygCiC7ePDJTe5Dpvz0KTTrNmgd23oFYhVdmH/Db0VpVnSV7RKYqSi4oBwFKFFpUiUQFKRSTEoIOeXoUkklTZM5gU91M0KULkVKFJoTgpBMHATwmBUgEAQKjtjB97TgGDwI6K6nlaL/bKpdlaJY01atVzg2N7f3Y6boHqq1XYGz2ul01jwFR7ngc9wndnnC6M0d9sBZlXsvJnfK1rXUEy/dVaNGj/AO3TY3oAPkikDRXKeyQwQCq9eG5I1rsbUMTUVTvFHF1xPinw+ArVBvMZIORJAHrdY7aV6taLC5OXLmpNdDfBXsL2Xr/mc6mJ5uP0Vw9k3Os6tA/0t+pKfplfoe0XNgVw+i0yJEj1WiVi4TsRhWP7wh73aue6P+LSG+i3e7AsBAHAKkl1yndfQSiUUhQISEJqIEzQpALUI0JnIkIZRQF74JKdtUkBmtUwhMKIHLnUSThMpNTBAKbSmCkAnIEkxTwr+Cw7SN7M6aLcx2zboPDUXBpd6ckCvj2jMrahZ+0tnU6n5mieUz6KtxsnDEvPLncft9jbFy5+rtZ1R26wEnRoJK6Z+w6DLlgJ5hTY0NHwtDRoAB8lCy75Ulk6ZGztjmz6/gz/APX2XQ0HrmNv9qKGGkVKjQ7+gGXn/auIx34o1iYw9FrRwdUJJ67osPMrWM/TXplk9qY5UNo9ocLQ/wA3EU2ci4b3/HNeAbS7T42tPeYmpB/lad1vSGxKxd034/Mqik/p/wB17btX8VsIye6bUqnkN0ebr+i5Gv8AirijV32taKcj/DImwz+K1yuB3CVAthPW1Z4cZ9Pp3ZmObXosrM/K9ocPEZKzC84/BnbW/RfhnG9M7zP7XG48D816SDw4rDkzx9ctJAJKSYha0wiQouU5UXNSoBSU0yRsqmVMOVdrlMFc8qiw1ymCgtUwtEI0ozSgMVbaG0mUmy434AZp70S/UqBolxgalcltPtfUY/epHda3UTvdeSydubZdUMu/LwaMv3WC/BV8RZo3GcXPJA8IEnyU7nbxFMfHPt6p2U7e4fGEUie6r8KbjZ8Z7juPTPqujxLl4zszZmEwkVnt7+q2HB9UkMYQZBZTbmcruK18ftuvWEveSCJ3fyiOg+q6Pmmtd1jLxzf9vTq9q7coUp337zh/Ky588gvO+0va7E1Zp0P8Jp/p/wAyNS/+UX4X5oW0MS2m0ueYAH1zjjpGvjGPsvatCu0tHw1CCTvZkjK/HWOZUt5XmdKYYyMKpgIN5JNyTeTx5zKc4OJFtF0tXBCSAOOeoIUBhB5R4mLiOMpfJkv7ud/hCQBll4pMwU+uSv7QxtKlaZOgueVuHjGaznY6s/8AK0MbfO58svRbnvR7JfwhGZHvkq2K3RNx6IzsM4mHuLo4EgAeVk7MG2xDR765pzKTutbrX/DXGmntClukQ+WOkx8JGp5gWX0AIXzYyhBBAg8OBnUdF7N+H/af+Jp93VI75g/5tH83XVa95a5/Pj/k7KFEp0oVHMjCaFKExCVAaSldJZNzzCitCFTZGasUwueKJhqpbR2oyiLm/AcSqnaTapotDWXe7LlzXLtY5xNSq+8XJyCWWWuIcxaFftBXebQxvmVXcXvdvOdn5/qsPHdrcLTO6N55H9ItPU5rKr9vCfyUOsk/RE8Wd+m9OzbQaDvRJ1N4KjXcSTc9PpkuMZ2nxtWO7pNieE26yfUopdtF2b2MvkA0+EwYT+OzuwadBUwrnvBqEd228SSXEZTwjz9VaqvABdkMyT9TwGfP5rkDs3Guu7EkdDHyiE3/APMl095iKjvHO+V/NPWP7PTO2xjXYyr3dOe7ac9eG900HPmtrB7NZTZu65248L/a6sYDZ1OgN1oHM5nz8kSu/r9uiM898To4pOdWpgiiQ4QYbUvHQz1zlY1Q4qpZ7txuRjM+OZ+S6EH09Pf0QrTJ487m/vJLHOw2ThdmNbwvqdUdlORMjrx08ldNISIkxpYZcSmYwcvfVK5W9tbVhSyz1tf2FE0if5fScwPfgtFxta3DKwN0mchPM/ZZ2NqDcM48NdfKOCNgjVovZUYS1zciM5+oN1oimSbXj7cdP1U20b6nhfgjkvZ6j2W7QtxVO/w1Ggb7eeo1C25XjuDc6m9tSm/deDb6gjjOS9N2FtgYhk2Dx+ZunMagrp8fk3xXLnjrmNRxTEqJKUqlYJJQnmkshiMap1aoY0uOQEpmLP23XAbu659Fz26VYVdznuNR+Zy/0tXGbeo4zFSabC2gMpIbvj+oiZI05LqNpVvgPMXjTTxyVXG7RNSkKdFrnvdYTuwzOZ0aMhI0Sxy1VIyez/ZJjG95Vh7iJA/lAj1K2mYCmMmsAB0/Th9FLZdJ1Ciym4gvaACQTEjKNVJ7j7/VGeVt5CG9GXvhZMcvfv8AdOXRnPkl9xp4XWAhOmvqqz3RN/fkjPdaP0v7kILhynTlAj7pnFZ88NP2tom3Z1MQef2lWDS9n1toiClbp7y+iD2pdzaR7KZzZHGOQJPRW3g2FuP6fZCLLXj7/ZA2qAZAa5efmpilfKfd/D7KyynwIlTo0/c8B+5sg9gNwtw0jRWhhYF75W4QrHd7o4T9VRxWJA1k/Py1Qzu1PEVSOMAcMgqRxcmx46XPHyQi8uHS5twtb3+iDvmYjM3+3jHqk1IJ/E30Gfpl5I+ye0VXD1A9jgd2xDrAjiJ+yqEa9LeWXgFnYmJIGQPHNbxnLWpXv2wtuUsXSFWmeTmnNjtCtBeCdlNvOwldtRoJaQA9gP5m5HxGY/de64bENqMa9hlrgHNOoIkLpl25fJ4/WiXSUd73KSTDFrVgxpceC5jEVy90k5+g5K9tTF7x3eA+azItz+i5srytIDWog5nonYIGdk8lBc71SNJz5/dQqC9s/Mjw5pZeJ+f7JNbzQEJ1/Q8veqfnPvwUjbhb9UJxz92QYb2qTR15W4J3jx9J+yVKMjx09+7oB2sjPz5ob3xIm/6cvmhVsTYwRbPz4e9Fn1a17mdAfog5F0PmbDkfDVFFE556T9lWwRJOvA3sOK0nuAHXK/SExeAe6A6Dz8EWnSgZDzTOfMfTwGSK9wb8uSCO5/E6Kpi6DTwvrbknq17wbjSM+ShVeOHuY/VIRSq0gAYPG/NVHu4NE3mT5ZImKPOVVNSAPizFwNElIas+SIGXHS6z8S2/S0x7KK+qCAJP2j6oNd/rHkq4TTUQ9+/Ret/hRtTvMO+gTLqLrf2Okjydvei8ja/mdF2H4VYvcx25wqMc2OY+If8AafNVnbHlm8XsadRSVHG8/qAbzjzPnKBUJy9+7om3B3QLy605RxLuvXyTVradSuLXLoVKpi06/VCi88OH1UxJJN1GAPfigJcDe2R9zy9FGYzk+7JN6fr9gok214n37yQDVX+/eSjvcknEzlf1yvfzTOM+GfvjxQaJIt79zKE5+uQn019/NTmBf04Dy6eyqOIr2iw46+GfPwSOK+NriIJ6gWEjIEcOnzWYK5Ls4n5cffNRxWI8zJ/f3qo7PMuz4xbw/VVmPG1ZOHX7Mp/CCRFvp8/so4qsJt+wPy9hE74Np2PhBPldcztjaRnUz43/AGWZN8ROTddLgxvDe4cPqdU1Ss0ayeHiFk7Sxxp02sBuGiY14+qHsPDGq7ezHNLV0PX7bdQjP2FRxVcDiIAVraFVrQZyHA8vmuS2hjTJg/pn6omNyuoMZsfF4smRMD1VN9fPdPD6qkah6mIRKVJ3FXnjmK2pBN4m3ikTOgR6VCyg6nF5kI9oWw2mF0HYR0bQw5/1kRyLHD6rAnKbLoewDZ2hh7fzH0Y7Tomzn1XukpJpTqjhebbSod84it8TQ8kNyFjaYN+Hp1TVHjXh76qxiJ33f3H5oT4Am3Lr7hcd7dAJbw9fCfqgVhAnj0KnUdx9801GprfheDbprN/AICuypxvxA53gwk8xpMW6wrLRN4E8Le/YQ6pdNhqLE2y4hBgl418tVHfERrwvkPrx18kVxMQAbCJ3n/fp7KBUrj+nrfkJFyffkkavVxADeEcT7+uqycdW56dbK/iKwjI+O552HX9FkYmoySYbx4OB/wC76cVrGNRm1xfp+9uUz6q/scEXOk5X6Dxn9FRcKc5GT/qd9jn7ur2DaIlhyvJM2vJNmq2f46bbGKxJcyxIEZwc4HvLXw4zGVpqXNpv53W5WqywgkGJGQItnGg8/RYeGwFSvVFOkJcT5D+ongOa14MZzaxlxOG1UpOxFfcbx6QBxME5LrC5uHpwLkepjRDw+EZhWENIL3fmeeJ05Cy53a2Onj+6jbuyQfkBtfaTnEwZ1v70WIXF3iVKqRKLhmX5rpxxmEU0NgsPJ1+i16GHtdLA0Y4cJ96q5iQBGp84ymMtVy+TP2rNvKnUbp7sq1ZufoiOqeEcBqoOeOceSU2atU/ddF+Hg/6hQ6u/8blgPpjh7uuj/Dhn/UKJ/v1/+Nyrss/xr2yTyST740SVXC8+xI+I8yfmq1XInT7SnSXHe3RFDGj+UGJGel2jL/dPgjU6gMW/aQ0eVvJOkgxaZkTqQI8/NA3gWzHCf/tH6ykkn9EjXdANvyifJ0eGf0VYtneZoY68UkkjZuLbAkE5A+f7fosbF0gHRwjU8D1TpLWPbcZdan8ZGdxc87qeIfusAGUb3oLeqSS6pzYdvBUjDd3O+Z53y8Su72Xs5mGpgMHxPbLn8TaY5C+SSSl5Kzkw9u4gg29+5XL4kmTJnh5JJLXgbx6QZT+S1cBQAjn9Ukk/Lbo66HD0hMcLJYxoECMxdMkuRP7ZNXWOHzQHi3j85+yZJVxUge7N+f1XWfhxT/8AXU+Qd/43Jklq9ln+NezJJJKm3A//2Q=="
             :alt "Nikola Tesla"}))
        (mui/mui-badge
          {:overlap      "circle"
           :anchorOrigin {:vertical "bottom" :horizontal "right"}
           :variant      "dot"
           :classes      {:badge (classes "styledBadge")}}
          (mui/mui-avatar
            {:src "data:image/jpeg;base64,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"
             :alt "Nikola Tesla"}))))))

(ws/defcard mui-avatars-card
  {::wsm/card-width  7
   ::wsm/card-height 10}
  (ct.fulcro/fulcro-card
    {::ct.fulcro/root MuiAvatars}))

(fp/defsc MuiBadges
  [this {:keys [value]}]
  {:initial-state (fn [_] {:value 30})
   :ident         (fn [] [::id "singleton"])
   :query         [:value]
   :use-hooks?    true}
  (let [classes (js->clj (use-styles))]
    (ui-theme-provider
      {:theme theme}
      (mui/mui-container
        {:className (classes "margin")}
        (mui/mui-badge {:badgeContent 4 :color "primary"} (icons/mail))
        (mui/mui-badge {:badgeContent 100
                        :color        "secondary"
                        :anchorOrigin {:vertical   "top"
                                       :horizontal "left"}} (icons/mail))
        (mui/mui-badge {:badgeContent 1000
                        :max          999
                        :color        "error"
                        :anchorOrigin {:vertical   "bottom"
                                       :horizontal "left"}} (icons/mail))
        (mui/mui-badge {:badgeContent 0 :color "primary"} (icons/mail))
        (mui/mui-badge {:badgeContent 0
                        :showZero     true
                        :color        "primary"
                        :anchorOrigin {:vertical   "bottom"
                                       :horizontal "right"}} (icons/mail))
        (mui/mui-badge {:variant "dot" :color "primary"} (icons/mail))
        (mui/mui-badge {:variant "dot" :classes {:badge (classes "styledBadge")}} (icons/mail))))))

(ws/defcard mui-badges-card
  {::wsm/card-width  7
   ::wsm/card-height 10}
  (ct.fulcro/fulcro-card
    {::ct.fulcro/root       MuiBadges
     ::ct.fulcro/wrap-root? true}))

(def chips [{:label "Basic"}
            {:label "Primary" :color "primary"}
            {:label "Secondary" :color "secondary"}
            {:label "Disabled" :disabled true}
            {:label "Clickable" :onClick (fn [])}
            {:label "Deletable" :onDelete (fn [])}
            {:label "Custom delete icon" :deleteIcon (icons/done) :onDelete (fn [])}
            {:label "Clickable deletable" :onDelete (fn []) :onClick (fn [])}
            {:label "Avatar" :avatar (mui/mui-avatar {:alt "a"})}
            {:label "Icon" :avatar (icons/favorite)}])

(fp/defsc MuiChips
  [this {:keys [value]}]
  {:initial-state (fn [_] {:value 30})
   :ident         (fn [] [::id "singleton"])
   :query         [:value]
   :use-hooks?    true}
  (let [classes (js->clj (use-styles))]
    (ui-theme-provider
      {:theme theme}
      (dom/div
        {:className (classes "chips")}
        (for [size ["medium" "small"]
              variant ["default" "outlined"]
              chip chips] (mui/mui-chip (assoc chip :variant variant :size size)))))))

(ws/defcard mui-chips-card
  {::wsm/card-width  7
   ::wsm/card-height 10}
  (ct.fulcro/fulcro-card
    {::ct.fulcro/root       MuiChips
     ::ct.fulcro/wrap-root? true}))

(fp/defsc MuiDividers
  [this {:keys [value]}]
  {:initial-state (fn [_] {:value 30})
   :ident         (fn [] [::id "singleton"])
   :query         [:value]
   :use-hooks?    true}
  (let [classes (js->clj (use-styles))]
    (ui-theme-provider
      {:theme theme}
      (mui/mui-list
        {:component "nav"
         :className (classes "dividers")}
        (mui/mui-list-item
          {:button true}
          (mui/mui-list-item-text
            {:primary "Inbox"}))
        (mui/mui-divider {})
        (mui/mui-list-item
          {:button  true
           :divider true}
          (mui/mui-list-item-text
            {:primary "Drafts"}))
        (mui/mui-list-item
          {:button true}
          (mui/mui-list-item-text
            {:primary "Thrash"}))
        (mui/mui-divider
          {:light true})
        (mui/mui-list-item
          {:button true}
          (mui/mui-list-item-text
            {:primary "Spam"}))))))

(ws/defcard mui-dividers-card
  {::wsm/card-width  7
   ::wsm/card-height 10}
  (ct.fulcro/fulcro-card
    {::ct.fulcro/root       MuiDividers
     ::ct.fulcro/wrap-root? true}))

(fp/defsc MuiIcons
  [this {:keys [value]}]
  {:initial-state (fn [_] {:value 30})
   :ident         (fn [] [::id "singleton"])
   :query         [:value]
   :use-hooks?    true}
  (let [classes (js->clj (use-styles))]
    (ui-theme-provider
      {:theme theme}
      (mui/mui-container
        {}
        (icons/delete)
        (icons/delete-outlined)
        (icons/delete-rounded)
        (icons/delete-sharp)
        (icons/delete-two-tone))
      (mui/mui-container
        {}
        (for [color ["inherit" "primary" "secondary" "action" "disabled"]]
          (icons/home {:key   color
                       :color color}))
        (icons/home {:style {:color "orange"}}))
      (mui/mui-container
        {}
        (for [fontSize ["small" "default" "large"]]
          (icons/home {:key      fontSize
                       :fontSize fontSize}))
        (icons/home {:style {:fontSize 42}}))
      (mui/mui-container
        {}
        (dom/link
          {:rel  "stylesheet"
           :href "https://fonts.googleapis.com/icon?family=Material+Icons"})
        ; https://material.io/resources/icons/?style=baseline
        (for [icon ["add_circle" "accessible_forward"]
              color ["inherit" "primary" "secondary"]]
          (mui/mui-icon {:key   (str icon color)
                         :color color} icon))))))

(ws/defcard mui-dividers-card
  {::wsm/card-width  7
   ::wsm/card-height 10}
  (ct.fulcro/fulcro-card
    {::ct.fulcro/root       MuiIcons
     ::ct.fulcro/wrap-root? true}))

(fp/defsc MuiLists
  [this {:keys [value]}]
  {:initial-state (fn [_] {:value 30})
   :ident         (fn [] [::id "singleton"])
   :query         [:value]
   :use-hooks?    true}
  (let [classes (js->clj (use-styles))
        [open setOpen] (hooks/use-state false)]
    (ui-theme-provider
      {:theme theme}
      (mui/mui-list
        {:className (classes "lists")
         :component "nav"
         :subheader (mui/mui-list-subheader {:component "div"} "List subheader")}
        (mui/mui-list-item
          {:button true}
          (mui/mui-list-item-icon {} (icons/send))
          (mui/mui-list-item-text {:primary "Sent mail"}))
        (mui/mui-list-item
          {:button true}
          (mui/mui-list-item-icon {} (icons/drafts))
          (mui/mui-list-item-text {:primary "Drafts"}))
        (mui/mui-list-item
          {:button  true
           :onClick (fn [] (setOpen (not open)))}
          (mui/mui-list-item-icon {} (icons/move-to-inbox))
          (mui/mui-list-item-text {:primary "Inbox"})
          (if open (icons/expand-less) (icons/expand-more)))
        (mui/mui-collapse
          {:in            open
           :timeout       "auto"
           :unmountOnExit true}
          (mui/mui-list
            {:component      "div"
             :disablePadding true}
            (mui/mui-list-item
              {:button    true
               :className (classes "nested")}
              (mui/mui-list-item-icon {} (icons/star-border))
              (mui/mui-list-item-text {:primary "Starred"})))))
      (mui/mui-list
        {:className (classes "lists")}
        (mui/mui-list-item
          {}
          (mui/mui-list-item-avatar {} (mui/mui-avatar {} (icons/send)))
          (mui/mui-list-item-text {:primary "Sent mail" :secondary "Jan 9, 2014"})
          (mui/mui-list-item-secondary-action {} (mui/mui-icon-button {:edge "end"} (icons/close))))
        (mui/mui-list-item
          {}
          (mui/mui-list-item-avatar {} (mui/mui-avatar {} (icons/drafts)))
          (mui/mui-list-item-text {:primary "Drafts" :secondary "Feb 10, 2015"}))
        (mui/mui-list-item-secondary-action {} (mui/mui-icon-button {:edge "end"} (icons/close)))
        (mui/mui-list-item
          {}
          (mui/mui-list-item-avatar {} (mui/mui-avatar {} (icons/move-to-inbox)))
          (mui/mui-list-item-text {:primary "Inbox" :secondary "Sep 11, 2016"})
          (mui/mui-list-item-secondary-action {} (mui/mui-icon-button {:edge "end"} (icons/close))))))))

(ws/defcard mui-lists-card
  {::wsm/card-width  7
   ::wsm/card-height 10}
  (ct.fulcro/fulcro-card
    {::ct.fulcro/root       MuiLists
     ::ct.fulcro/wrap-root? true}))

(fp/defsc MuiSimpleTable
  [this {:keys [value]}]
  {:initial-state (fn [_] {:value 30})
   :ident         (fn [] [::id "singleton"])
   :query         [:value]
   :use-hooks?    true}
  (let [classes (js->clj (use-styles))]
    (ui-theme-provider
      {:theme theme}
      (mui/mui-table-container
        {:component material-paper}
        (mui/mui-table
          {:className (classes "tables")}
          (mui/mui-table-head
            {}
            (mui/mui-table-row
              {}
              (mui/mui-table-cell {} "Name")
              (for [head ["Column 1" "Column 2" "Column 3" "Column 4"]]
                (mui/mui-table-cell {:key "head" :align "right"} head))))
          (mui/mui-table-body
            {}
            (for [row ["a" "b" "c" "d" "e" "f"]]
              (mui/mui-table-row
                {}
                (mui/mui-table-cell {} row)
                (for [cell [1 2 3 4]]
                  (mui/mui-table-cell {:align "right"} cell))))))))))

(ws/defcard mui-simple-table-card
  {::wsm/card-width  7
   ::wsm/card-height 20}
  (ct.fulcro/fulcro-card
    {::ct.fulcro/root       MuiSimpleTable
     ::ct.fulcro/wrap-root? true}))

(def use-toolbar-styles
  (make-styles (fn [theme]
                 (let [spacing (get theme "spacing")]
                   {:root      {:paddingLeft  (spacing 2)
                                :paddingRight (spacing 1)}
                    :highlight {:color           (get-in theme ["palette" "secondary" "main"])
                                :backgroundColor (lighten (get-in theme ["palette" "secondary" "light"]) 0.85)}
                    :title     {:flex "1 1 100%"}}))))

(fm/defmutation delete-selected-row-items [_]
  (action
    [{:keys [state]}]
    (let [selected-row-item-names (->> (get @state :name)
                                       (filter (fn [[_ item]] (:selected item)))
                                       (mapv first))]
      (swap! state update-in
             [::id :singleton :row-items]
             (fn filter-selected [row-items]
               (->> row-items
                    (filterv (fn [[_ row-item-name]]
                               (->> selected-row-item-names
                                    (some (set row-item-name))
                                    not)))))))))

(fp/defsc TableToolbar [this {:keys [selected-count]}]
  {:initial-state (fn [_] {})
   :ident         (fn [] [::id :singleton])
   :query         [:selected-count]
   :use-hooks?    true}
  (let [classes (js->clj (use-toolbar-styles))]
    (mui/mui-toolbar
      {:className [(classes "root") (when (> selected-count 0) (classes "highlight"))]}
      (if (> selected-count 0)
        (mui/mui-typography
          {:className (classes "title")
           :color     "inherit"
           :variant   "subtitle1"
           :component "div"}
          (str selected-count " selected"))
        (mui/mui-typography
          {:className (classes "title")
           :variant   "h6"
           :id        "tableTitle"
           :component "div"}
          "Nutrition"))
      (when (> selected-count 0)
        (mui/mui-tooltip
          {:title "Delete"}
          (mui/mui-icon-button
            {:onClick (fn []
                        (comp/transact! this [`(delete-selected-row-items {})]))}
            (icons/delete)))))))

(def ui-table-toolbar (fp/factory TableToolbar))

(fm/defmutation toggle-check-all [{:keys [check-all?]}]
  (action
    [{:keys [state]}]
    (mapv (fn [[name]]
            (swap! state assoc-in [:name name :selected] check-all?))
          (get @state :name))))

(fp/defsc TableHead [this {:keys [selected-count row-count]}]
  {:ident      (fn [] [::id :singleton])
   :query      [:selected-count :row-count]
   :use-hooks? true}
  (let [all-checked? (and (not= selected-count 0)
                          (= row-count selected-count))
        some-checked? (and (not= selected-count 0)
                           (> row-count selected-count))]
    (mui/mui-table-head
      {}
      (mui/mui-table-row
        {}
        (mui/mui-table-cell
          {:padding "checkbox"}
          (mui/mui-checkbox
            {:indeterminate some-checked?
             :checked       all-checked?
             :onChange      (fn []
                              (comp/transact! this [`(toggle-check-all ~{:check-all? (not all-checked?)})]))}))
        (for [{:keys [name align]} [{:name "A" :align "left"}
                                    {:name "B" :align "right"}
                                    {:name "C" :align "right"}
                                    {:name "D" :align "right"}
                                    {:name "E" :align "right"}]]
          (mui/mui-table-cell
            {:key     name
             :align   align
             :padding (if (= align "right") "default" "none")}
            (mui/mui-table-sort-label
              {:active    false
               :direction "asc"
               :onClick   (fn [])}
              name)))))))

(def ui-table-head (fp/factory TableHead))

(fp/defsc RowItem [this {:keys [name a b c d selected] :as props}]
  {:initial-state (fn [props] (merge {:name "a" :a 1 :b 2 :c 3 :d 4 :selected false}
                                     props))
   :ident         :name
   :query         [:name :a :b :c :d :selected]}
  (mui/mui-table-row
    {:hover    true
     :onClick  (fn [] (fm/set-value! this :selected (not selected)))
     :role     "checkbox"
     :tabIndex -1
     :selected selected}
    (mui/mui-table-cell
      {:padding "checkbox"
       :key     5}
      (mui/mui-checkbox
        {:checked selected}))
    (mui/mui-table-cell
      {:key       0
       :component "th"
       :id        name
       :scope     "row"
       :padding   "none"}
      name)
    (for [cell [a b c d]]
      (mui/mui-table-cell {:key cell :align "right"} cell))))

(def ui-row-item (fp/factory RowItem {:keyfn :name}))

(fp/defsc MuiComplexTable [this {:keys [row-items]}]
  {:initial-state (fn [_] {:row-items (mapv #(fp/get-initial-state RowItem {:name %})
                                            ["a" "b" "c" "d" "e"])})
   :ident         (fn [] [::id :singleton])
   :query         [{:row-items (fp/get-query RowItem)}]
   :use-hooks?    true}
  (let [classes (js->clj (use-styles))
        row-count (count row-items)
        selected-count (->> row-items
                            (filter :selected)
                            (count))]
    (ui-theme-provider
      {:theme theme}
      (dom/div
        {:className (classes "selectTable")}
        (mui/mui-paper
          {:className (classes "tablePaper")}
          (ui-table-toolbar {:row-count      row-count
                             :selected-count selected-count})
          (mui/mui-table-container
            {}
            (mui/mui-table
              {:className (classes "tables")}
              (ui-table-head {:row-count      row-count
                              :selected-count selected-count})
              (mui/mui-table-body
                {}
                (mapv ui-row-item row-items)))))))))

(ws/defcard mui-complex-table-card
  {::wsm/card-width  7
   ::wsm/card-height 15}
  (ct.fulcro/fulcro-card
    {::ct.fulcro/root MuiComplexTable}))

(fp/defsc MuiTooltips [this {:keys [value]}]
  {:initial-state (fn [_] {:value 30})
   :ident         (fn [] [::id "singleton"])
   :query         [:value]
   :use-hooks?    true}
  (ui-theme-provider
    {:theme theme}
    (mui/mui-container
      {}
      (for [position ["left" "top-start" "top" "bottom" "bottom-end" "right"]]
        (mui/mui-tooltip
          {:title     "Delete"
           :placement position
           :key       position}
          (mui/mui-button
            {}
            position))))
    (mui/mui-container
      {}
      (for [position ["left" "top-start" "top" "bottom" "bottom-end" "right"]]
        (mui/mui-tooltip
          {:title     "Delete"
           :arrow     true
           :placement position
           :key       position}
          (mui/mui-button
            {}
            position))))))

(ws/defcard mui-tooltips-card
  {::wsm/card-width  7
   ::wsm/card-height 15}
  (ct.fulcro/fulcro-card
    {::ct.fulcro/root MuiTooltips}))

(fp/defsc MuiTypographys [this {:keys [value]}]
  {:initial-state (fn [_] {:value 30})
   :ident         (fn [] [::id "singleton"])
   :query         [:value]
   :use-hooks?    true}
  (ui-theme-provider
    {:theme theme}
    (mui/mui-container
      {}
      (for [variant ["h1" "h2" "h3" "h4" "h5" "h6" "subtitle1" "subtitle2" "body1" "body2" "button" "caption" "overline"]]
        (mui/mui-typography
          {:key     variant
           :variant variant
           :display "block"}
          variant)))))

(ws/defcard mui-typographys-card
  {::wsm/card-width  7
   ::wsm/card-height 15}
  (ct.fulcro/fulcro-card
    {::ct.fulcro/root MuiTypographys}))