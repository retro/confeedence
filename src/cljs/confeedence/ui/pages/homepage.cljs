(ns confeedence.ui.pages.homepage
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.css.core :refer-macros [defelement]]))

(defelement -wrap
  :class [:my4 :mx-auto]
  :style {:width "64rem"})

(defelement -logo-img
  :tag :img
  :class [:mx-auto :block]
  :style {:max-width "61.8%"})

(defn render [ctx]
  [-wrap
   [-logo-img {:src "/images/logo.svg" }]
   [:h2.dark.mt4.mb2 "Confeedence"]
   [:p.mb1 "Confeedence is a simple app that allows you to easily build conference landing pages. It is built on top of the " [:a.c-blue {:href "http://whenhub.com" :target "_blank"} "WhenHub's API"] " as an entry for the " [:a.c-blue {:href "https://whenhub.devpost.com/" :target "_blank"} "WhenHub Hackathon."]]
   [:p "Confeedence was built by " [:a.c-blue {:href "https://github.com/tiborkr" :target "_blank"} "Tibor Kranjčec"] " and " [:a.c-blue {:href "https://github.com/retro"} "Mihael Konjević"] " in ClojureScript, using the " [:a.c-blue {:href "http://keechma.com" :target "_blank"} "Keechma Framework."]]
   [:p "You can find the source code on " [:a.c-blue {:href "https://github.com/retro/confeedence/"} "GitHub"]]
   [:h2.dark.mt4.mb2 "Demo"]
   [:iframe {:width 640 :height 360 :src "https://www.youtube.com/embed/zmgFkUTM6gU" :frame-border "0" :allow-full-screen true}]
   [:div.center.my6.pt4
    [:a.bg-green.rounded.py2.px4.c-large.text-decoration-none.c-white {:href (ui/url ctx {:page "edit"})} "Try it out"]]
   [:p.c-small
    "* You will need a WhenHub access token to continue. You can find more information about the access tokens " [:a.c-blue {:href "https://developer.whenhub.com/v1.0/docs/connecting-to-the-api" :target "_blank"} "here"] "."]
   [:div.clearfix.pb2]])

(def component
  (ui/constructor {:renderer render}))


