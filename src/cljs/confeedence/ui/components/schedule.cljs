(ns confeedence.ui.components.schedule
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer [sub> route>]]
            [keechma.toolbox.css.core :refer-macros [defelement]]
            [keechma.toolbox.forms.helpers :as forms-helpers]
            [confeedence.stylesheets.colors :refer [theme-colors-by-slug]]))

(defelement -action-link
  :tag :a
  :class [:bg-blue :c-white :px1 :py0-5 :rounded :text-decoration-none :inline-block])

(defelement main-wrap
  :class [:flex-grow])

(defelement center-div
  :class [:center])

(defelement conference-info-wrap)

(defelement title-center
  :tag :h1
  :class [:center])

(defelement conference-description
  :tag :p
  :class [:c-large :center])

(defelement events-wrap)

(defelement subtitle-center
  :tag :h2
  :class [:center])

(defelement talks-wrap)

(defelement talks-column-wrap
  :class [:flex :justify-center])

(defn get-tag [conference tag]
  (get-in conference [:confeedence-tags tag]))

(defn get-color [conference tag]
  (let [val (get-tag conference tag)]
    (get theme-colors-by-slug val)))

(defn render-events [ctx events]
  (println events)
  (let [current-route (route> ctx)]
    [:ul
     (doall (map (fn [e]
                   [:li {:key (:id e)}
                    (:name e)
                    [:br]
                    (:description e)
                    [:a
                     {:href (ui/url ctx (assoc current-route :form {:type "event" :id (:id e)}))}
                     "Edit Event"]]) events))]))

(defn render [ctx]
  (let [current-route (route> ctx)
        form-props [:schedule (:id current-route)]
        form-data (:data @(forms-helpers/form-state ctx form-props))
        conference (or form-data (sub> ctx :current-schedule))
        track-count (js/parseInt (get-in conference [:confeedence-tags :track-count]))]


    [main-wrap {:style {:background-color (get-color conference :main-bg-color)}}
     [conference-info-wrap
      [title-center {:style {:color (get-color conference :main-heading-color)}} (:name conference)]
      [conference-description {:style {:color (get-color conference :main-text-color)}} (:description conference)]
      [center-div
       [-action-link {:href (ui/url ctx (assoc current-route :form {:type "conference"}))} "Edit Conference Info"]]]
     [events-wrap {:style {:background-color (get-color conference :events-bg-color)}}
      [subtitle-center {:style {:color (get-color conference :events-heading-color)}} "Events"]
      [render-events ctx (sub> ctx :current-schedule-events)]
      [center-div
       [-action-link {:href (ui/url ctx (assoc current-route :form {:type "event"}))} "Add New event"]]]
     [talks-wrap {:style {:background-color (get-color conference :talks-bg-color)}}
      [subtitle-center {:style {:color (get-color conference :talks-heading-color)}} "Talks"]
      [talks-column-wrap
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
                   :subscription-deps [:current-schedule :form-state :current-schedule-events]}))
