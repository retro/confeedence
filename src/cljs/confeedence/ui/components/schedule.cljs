(ns confeedence.ui.components.schedule
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer [sub> route>]]
            [keechma.toolbox.css.core :refer-macros [defelement]]
            [keechma.toolbox.forms.helpers :as forms-helpers]
            [confeedence.stylesheets.colors :refer [theme-colors-by-slug]]))

(defelement -action-link
  :tag :a
  :class [:bg-blue :c-white :px1 :py0-5 :rounded :text-decoration-none :inline-block])

(defn get-tag [conference tag]
  (get-in conference [:confeedence-tags tag]))

(defn get-color [conference tag]
  (let [val (get-tag conference tag)]
    (get theme-colors-by-slug val)))

(defn render [ctx]
  (let [current-route (route> ctx)
        form-props [:schedule (:id current-route)]
        form-data (:data @(forms-helpers/form-state ctx form-props))
        conference (or form-data (sub> ctx :current-schedule))
        track-count (js/parseInt (get-in conference [:confeedence-tags :track-count]))]


    [:div.flex-grow {:style {:background-color (get-color conference :main-bg-color)}}
     [:div 
      [:h1.center {:style {:color (get-color conference :main-heading-color)}} (:name conference)]
      [:p.center {:style {:color (get-color conference :main-text-color)}} (:description conference)]
      [:div.center
       [-action-link {:href (ui/url ctx (assoc current-route :form {:type "conference"}))} "Edit Conference Info"]]]
     [:div {:style {:background-color (get-color conference :events-bg-color)}}
      [:h2.center {:style {:color (get-color conference :events-heading-color)}} "Events"]
      [:div.center
       [-action-link {:href "#"} "Add New event"]]]
     [:div {:style {:background-color (get-color conference :talks-bg-color)}}
      [:h2.center {:style {:color (get-color conference :talks-heading-color)}} "Talks"]
      [:div.flex.justify-center
       (doall (map (fn [idx]
                     [:div.pb2
                      {:key idx
                       :style {:background-color (get-color conference :talks-track-bg-color)
                               :width "300px"
                               :margin "0 20px"}}
                      [:div.center
                       [:h3
                        {:style {:color (get-color conference :talks-track-heading-color)}}
                        "Track #" (inc idx)]
                       [-action-link {:href "#"} "Add New Talk"]]]) (range 0 track-count)))]]]))

(def component
  (ui/constructor {:renderer render
                   :subscription-deps [:current-schedule :form-state]}))
