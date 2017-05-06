(ns confeedence.ui.components.schedule
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer [sub> route>]]
            [keechma.toolbox.css.core :refer-macros [defelement]]
            [keechma.toolbox.forms.helpers :as forms-helpers]
            [cljsjs.moment]
            [confeedence.stylesheets.colors :refer [theme-colors-by-slug]]
            [medley.core :refer [dissoc-in]]
            [keechma.toolbox.util :refer [class-names]]))

(defn format-date [date]
  (.format (js/moment date) "ll"))

(defn format-full-date [date]
  (.format (js/moment date) "llll"))

(defn format-time-only [date]
  (.format (js/moment date) "hh:mm a"))

(defelement -action-link
  :tag :a
  :class [:bg-blue :c-white :px1 :py0-5 :rounded :text-decoration-none :inline-block :border-width-2 :bd-white :border])

(defelement main-wrap
  :class [:relative :flex :flex-column :flex-auto]
  :style [{:overflow-y "auto"
           :min-height "100%"}])

(defelement center-div
  :class [:center])

(defelement conference-info-wrap
  :class [:py6])

(defelement title-center
  :tag :h1
  :class [:center :mt0 :mb1]
  :style {:font-size "8rem"})

(defelement conference-description
  :tag :p
  :class [:c-large :center :px4 :mx-auto]
  :style [{:max-width "120rem"}])

(defelement events-wrap
  :class [:pb4])

(defelement subtitle-center
  :tag :h2
  :class [:center])

(defelement talks-wrap)

(defelement talks-column-wrap
  :class [:flex :justify-center :pb4])

(defelement date-circle
  :tag :span
  :class [:absolute :left-0 :right-0 :mx-auto :circle :flex :items-center :justify-center :c-white :bg-blue :c-button :center]
  :style [{:width "7rem"
           :height "7rem"}])

(defelement timeline-wrap
  :tag :ul
  :class [:mx-auto :clearfix :list-reset :relative]
  :style [{:max-width "120rem"}])

(defelement timeline-item
  :tag :li
  :class [:clearfix :pb2 :relative])

(defelement timeline-line
  :class [:absolute]
  :style [{:top 0
           :left "50%"
           :margin-left "-1px"
           :width "2px"
           :bottom 0}
          [:&.last-ev {:bottom "auto"
                       :height "20px"}]])

(defelement timeline-item-wrap-left
  :class [:bg-lightest-gray :left :px2 :py1 :relative]
  :style [{:border-radius "0.2rem"
           :width "42%"
           :min-height "7rem"
           :margin-left "3%"}
          [:&:after {:content "''"
                     :display "block"
                     :position "absolute"
                     :right "-9px"
                     :top "25px"
                     :width 0
                     :height 0
                     :border-style "solid"
                     :border-width "10px 0 10px 10px"
                     :border-top-color "transparent"
                     :border-right-color "transparent"
                     :border-bottom-color "transparent"
                     :border-left-color "currentColor"}]])

(defelement timeline-item-wrap-right
  :class [:bg-lightest-gray :right :px2 :py1 :relative]
  :style [{:border-radius "0.2rem"
           :width "42%"
           :min-height "7rem"
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
                      :border-top-color "transparent"
                      :border-right-color "currentColor"
                      :border-bottom-color "transparent"
                      :border-left-color "transparent"}]])

(defelement event-title
  :tag :h2
  :class [:mt0 :mb0])

(defelement event-description
  :tag :p
  :class [:mt0-5])

(defelement talk-wrap
  :class [:my0 :mx2 :center]
  :style [{:width "350px"}])

(defn group-events [events]
  (reduce (fn [acc e]
            (let [type (keyword (get-in e [:confeedence :custom-fields :type]))
                  type-events (get acc type)]
              (assoc acc type (conj type-events e)))) {} events))

(defn group-talks-by-track [events track-count]
  (reduce (fn [acc e]
            (let [track (js/parseInt (or (get-in e [:confeedence :custom-fields :track]) "0"))
                  track-exists? (<= 1 track track-count)
                  track-key (if track-exists? track :unassigned)
                  tracks (or (get acc track-key) [])]
              (assoc acc track-key (conj tracks e)))) {} events))

(defn get-tag [conference tag]
  (get-in conference [:confeedence-tags tag]))

(defn get-color [conference tag]
  (let [val (get-tag conference tag)]
    (get theme-colors-by-slug val)))

(defn sort-events [events]
  (sort-by (fn [e] (.unix (js/moment (get-in e [:when :startDate])))) events))

(defn make-end-event [event]
  (let [end-date (get-in event [:when :endDate])
        end-timezone (get-in event [:when :endTimezone])]
    (-> event
        (assoc :description ""
               :name (str (:name event) " (End)")
               :event-end? true)
        (assoc-in [:when :startDate] end-date)
        (assoc-in [:when :startTimezone] end-timezone)
        (dissoc-in [:when :endDate])
        (dissoc-in [:when :endTimezone]))))

(defn prepare-timeline [events]
  (sort-events (reduce (fn [acc [idx event]]
                         (let [side (if (= 0 (mod idx 2)) :left :right)
                               event-with-side (assoc event :timeline-side side)]
                           (if (get-in event [:when :endDate])
                             (concat acc [event-with-side
                                          (make-end-event event-with-side)])
                             (conj acc event-with-side))))
                       [] (map-indexed (fn [idx event] [idx event]) (sort-events events)))))

(defn render-events [ctx conference events show-action-links?] 
  (let [current-route (route> ctx)
        first-ev (first events)
        last-ev (last events)]
    [timeline-wrap
     (doall
      (map (fn [e]
             [timeline-item {:key [(:id e) (:event-end? e)]}
              [timeline-line {:style {:background-color (get-color conference :events-timeline-bg-color)}
                              :class (class-names {:last-ev (= e last-ev)})}]
              [(if (= :left (:timeline-side e)) timeline-item-wrap-left timeline-item-wrap-right)
               {:style {:background-color (get-color conference :events-callout-bg-color)
                        :color (get-color conference :events-callout-bg-color)}}
               [event-title {:style {:color (get-color conference :events-callout-heading-color)}} (:name e)]
               (when (seq (:description e))
                 [event-description {:style {:color (get-color conference :events-callout-text-color)}} (:description e)])
               (when show-action-links?
                 [-action-link
                  {:href (ui/url ctx (assoc current-route :form {:type (get-in e [:confeedence :custom-fields :type]) :id (:id e)}))} 
                  "Edit Event"])]
              [date-circle {:style {:background-color (get-color conference :events-timeline-bg-color)
                                    :color (get-color conference :events-timeline-text-color)}}
               (format-date (get-in e [:when :startDate]))]]) events))]))


(defn render-talk [ctx conference talk last-talk show-action-links?]
  (let [current-route (route> ctx)
        last? (= talk last-talk)]
    (let [photo-url (get-in talk [:confeedence :custom-fields :speaker-photo-url])]
      [:div.pb3.px2 (when (not last?) {:style {:border-bottom "1px solid"
                                               :border-bottom-color (get-color conference :talks-bg-color)}})
       [:p.c-large.mb1 {:style {:color (get-color conference :talks-talk-heading-color)}} (:name talk)]
       [:p.c-small.m0-5.italic {:style {:color (get-color conference :talks-talk-text-color)}} (format-date (get-in talk [:when :startDate]))]
       [:p.c-small.m0.italic {:style {:color (get-color conference :talks-talk-text-color)}} (str (format-time-only (get-in talk [:when :startDate])) " - " (format-time-only (get-in talk [:when :endDate])))]
       [:p.c-medium {:style {:color (get-color conference :talks-talk-text-color)}} (get-in talk [:confeedence :custom-fields :description])]
       [:img {:src (if (seq photo-url) photo-url "/img/avatar.png")
              :class "circle"
              :style {:height "5rem" 
                      :width "5rem"}}]
       [:p.c-body.my0-5 {:style {:color (get-color conference :talks-talk-heading-color)}} (get-in talk [:confeedence :custom-fields :speaker-name])]
       [:p.c-body.my0-5 {:style {:color (get-color conference :talks-talk-heading-color)}} (get-in talk [:confeedence :custom-fields :speaker-bio])] 
       (when show-action-links?
         [:div.mt2
          [-action-link {:href (ui/url ctx (assoc current-route :form {:type "talk" :id (:id talk)}))} "Edit Talk"]])])))

(defn render-talks [ctx conference track-name talks show-action-links?]
  (let [last-talk (last talks)]
    [talk-wrap
     {:key track-name
      :style {:background-color (get-color conference :talks-track-bg-color)}} 
     [:h2
      {:style {:color (get-color conference :talks-track-heading-color)}}
      track-name]
     [:div 
      (doall (map (fn [t]
                    ^{:key (:id t)}
                    [render-talk ctx conference t last-talk show-action-links?]) talks))]]))


(defn render [ctx]
  (let [current-route (route> ctx)
        form-props [:schedule (:id current-route)]
        form-data (:data @(forms-helpers/form-state ctx form-props))
        conference (or form-data (sub> ctx :current-schedule))
        track-count (js/parseInt (get-in conference [:confeedence-tags :track-count]))
        grouped-events (group-events (sub> ctx :current-schedule-events))
        timeline-events (prepare-timeline (concat (:event grouped-events) (:news grouped-events)))
        grouped-talks (group-talks-by-track (sort-events (:talk grouped-events)) track-count)
        show-action-links? (= "edit" (:page current-route))]

    [main-wrap {:style {:background-color (get-color conference :main-bg-color)}}
     [conference-info-wrap
      [title-center {:style {:color (get-color conference :main-heading-color)}} (:name conference)]
      [conference-description {:style {:color (get-color conference :main-text-color)}} (:description conference)]
      (when show-action-links?
        [center-div
         [-action-link {:href (ui/url ctx (assoc current-route :form {:type "conference"}))} "Edit Conference Info"]])]
     [events-wrap {:style {:background-color (get-color conference :events-bg-color)}}
      [subtitle-center {:style {:color (get-color conference :events-heading-color)}} "Events"]
      [render-events ctx conference timeline-events show-action-links?]
      (when show-action-links?
        [center-div
         [-action-link {:href (ui/url ctx (assoc current-route :form {:type "event"})) :class "mr1"} "Add New event"]
         [-action-link {:href (ui/url ctx (assoc current-route :form {:type "news"})) :class "ml1"} "Add News"]])]
     [talks-wrap {:style {:background-color (get-color conference :talks-bg-color)}}
      [subtitle-center {:style {:color (get-color conference :talks-heading-color)}} "Talks"]
      [talks-column-wrap
       (doall (map (fn [idx]
                     ^{:key idx}
                     [render-talks ctx conference (str "Track #" (inc idx)) (get grouped-talks (inc idx)) show-action-links?])
                   (range 0 track-count)))
       (when (seq (:unassigned grouped-talks))
         [render-talks ctx conference "Unassigned Talks" (:unassigned grouped-talks) show-action-links?])]
      (when show-action-links?
        [:div.center.pb2 [-action-link
                          {:href (ui/url ctx (assoc current-route :form {:type "talk"}))}
                          "Add New Talk"]])]]))

(def component
  (ui/constructor {:renderer render
                   :subscription-deps [:current-schedule :form-state :current-schedule-events]}))
