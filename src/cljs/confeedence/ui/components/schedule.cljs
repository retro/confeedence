(ns confeedence.ui.components.schedule
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer [sub> route>]]
            [keechma.toolbox.css.core :refer-macros [defelement]]
            [keechma.toolbox.forms.helpers :as forms-helpers]
            [cljsjs.moment]
            [confeedence.stylesheets.colors :refer [theme-colors-by-slug]]))

(defn format-date [date]
  (.format (js/moment date) "ll"))

(defelement -action-link
  :tag :a
  :class [:bg-blue :c-white :px1 :py0-5 :rounded :text-decoration-none :inline-block])

(defelement main-wrap
  :class [:relative :flex :flex-column :flex-auto]
  :style [{:overflow-y "auto"}])

(defelement center-div
  :class [:center])

(defelement conference-info-wrap
  :class [])

(defelement title-center
  :tag :h1
  :class [:center])

(defelement conference-description
  :tag :p
  :class [:c-large :center :px4])

(defelement events-wrap)

(defelement subtitle-center
  :tag :h2
  :class [:center])

(defelement talks-wrap)

(defelement talks-column-wrap
  :class [:flex :justify-center])

(defelement date-circle
  :tag :span
  :class [:absolute :left-0 :right-0 :mx-auto :circle :flex :items-center :justify-center :c-white :bg-blue :c-button :center]
  :style [{:width "7rem"
           :height "7rem"}])

(defelement timeline-item-wrap-left
  :class [:bg-lightest-gray :left :p2 :relative]
  :style [{:border-radius "0.2rem"
           :width "38%"
           :min-height "10rem"
           :margin-left "3%"
           :margin-right "9%"}
          [:&:after {:content "''"
                     :display "block"
                     :position "absolute"
                     :right "-9px"
                     :top "25px"
                     :width 0
                     :height 0
                     :border-style "solid"
                     :border-width "10px 0 10px 10px"
                     :border-color "transparent transparent transparent #ffffff"}]])

(defelement timeline-item-wrap-right
  :class [:bg-lightest-gray :right :p2 :relative]
  :style [{:border-radius "0.2rem"
           :width "38%"
           :min-height "10rem"
           :margin-left "9%"
           :margin-right "3%"}
          [:&:before {:content "''"
                     :display "block"
                     :position "absolute"
                     :left "-10px"
                     :top "25px"
                     :width 0
                     :height 0
                     :border-style "solid"
                     :border-width "10px 10px 10px 0"
                     :border-color "transparent #ffffff transparent transparent"}]])

(defn get-tag [conference tag]
  (get-in conference [:confeedence-tags tag]))

(defn get-color [conference tag]
  (let [val (get-tag conference tag)]
    (get theme-colors-by-slug val)))

(defn render-events [ctx events] 
  (let [current-route (route> ctx)]
    [:ul.max-width-4.mx-auto.clearfix.list-reset.relative
     (doall (map-indexed (fn [i,e]
                           (if (= 0 (mod i 2))
                             [:li.clearfix {:key (:id e)}
                              [timeline-item-wrap-left
                               (:name e)
                               [:br]
                               [:p (:description e)]
                               [:br]
                               [:a
                               {:href (ui/url ctx (assoc current-route :form {:type "event" :id (:id e)}))}
                               "Edit Event"]]
                              [date-circle
                               (format-date (get-in e [:when :startDate]))]
                              ]
                             [:li.clearfix {:key (:id e)}
                              [timeline-item-wrap-right
                               (:name e)
                               [:br]
                               [:p (:description e)]
                               [:br]
                               [:a
                               {:href (ui/url ctx (assoc current-route :form {:type "event" :id (:id e)}))}
                               "Edit Event"]]
                              [date-circle
                               (format-date (get-in e [:when :startDate]))]
                              ])) events))]))

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
