(ns confeedence.ui.components.schedule
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer [sub> route>]]
            [keechma.toolbox.css.core :refer-macros [defelement]]))

(defelement -action-link
  :tag :a
  :class [:bg-blue :c-white :px1 :py0-5 :rounded :text-decoration-none :inline-block])

(defn render [ctx]
  (let [conference (sub> ctx :current-schedule)
        current-route (route> ctx)
        track-count (js/parseInt (get-in conference [:confeedence-tags :track-count]))]
    [:div.flex-grow
     [:h1.center (:name conference)]
     [:p.center (:description conference)]
     [:div.center
      [-action-link {:href (ui/url ctx (assoc current-route :form {:type "conference"}))} "Edit Conference Info"]]
     [:hr]
     [:h2.center "Events"]
     [:div.center
      [-action-link {:href "#"} "Add New event"]]
     [:hr]
     [:h2.center "Talks"]
     [:div.flex.justify-center
      (doall (map (fn [idx]
                    [:div.bg-light-gray.pb2 {:key idx :style {:width "300px" :margin "0 20px"}}
                     [:div.center
                      [:h3 "Track #" (inc idx)]
                      [-action-link {:href "#"} "Add New Talk"]]]) (range 0 track-count)))]]))

(def component
  (ui/constructor {:renderer render
                   :subscription-deps [:current-schedule]}))
