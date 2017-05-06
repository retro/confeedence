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

(defelement -action-link
  :tag :a
  :class [:bg-blue :c-white :px1 :py0-5 :rounded :text-decoration-none :inline-block :border-width-2 :bd-white :border])

(defelement main-wrap
  :class [:relative :flex :flex-column :flex-auto]
  :style [{:overflow-y "auto"}])

(defelement center-div
  :class [:center])

(defelement conference-info-wrap
  :class [:pb4])

(defelement title-center
  :tag :h1
  :class [:center])

(defelement conference-description
  :tag :p
  :class [:c-large :center :px4])

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

(defelement timeline-line
  :class [:bg-blue :absolute]
  :style [{:top 0
           :left "50%"
           :margin-left "-1px"
           :width "2px"
           :bottom 0}
          [:&.last-ev {:bottom "auto"
                       :height "20px"}]])

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

(defn render-events [ctx events] 
  (let [current-route (route> ctx)
        first-ev (first events)
        last-ev (last events)]
    [:ul.max-width-4.mx-auto.clearfix.list-reset.relative
     (doall
      (map (fn [e]
             [:li.clearfix.pb2.relative {:key [(:id e) (:event-end? e)]}
              [timeline-line {:class (class-names {:last-ev (= e last-ev)})}]
              [(if (= :left (:timeline-side e)) timeline-item-wrap-left timeline-item-wrap-right)
               (:name e)
               [:br]
               [:p (:description e)]
               [:br]
               [-action-link
                {:href (ui/url ctx (assoc current-route :form {:type (get-in e [:confeedence :custom-fields :type]) :id (:id e)}))} 
                "Edit Event"]]
              [date-circle
               (format-date (get-in e [:when :startDate]))]]) events))]))


(defn render-talk [ctx conference talk]
  (let [current-route (route> ctx)]
    (let [photo-url (get-in talk [:confeedence :custom-fields :speaker-photo-url])]
      [:div.mb4
       [:div "Talk Name: "  (:name talk)]
       [:div "Description: " (get-in talk [:confeedence :custom-fields :description])]
       [:div "Speaker Name: " (get-in talk [:confeedence :custom-fields :speaker-name])]
       [:div "Speaker Bio: " (get-in talk [:confeedence :custom-fields :speaker-bio])]
       [:img {:src (if (seq photo-url) photo-url "/img/avatar.png")
              :style {:height "40px" :width "40px"}}]
       [:div
        [-action-link {:href (ui/url ctx (assoc current-route :form {:type "talk" :id (:id talk)}))} "Edit Talk"]]])))

(defn render-talks [ctx conference track-name talks]
  [:div.pb2
   {:key track-name
    :style {:background-color (get-color conference :talks-track-bg-color)
            :width "300px"
            :margin "0 20px"}}
   [:div.center
    [:h3
     {:style {:color (get-color conference :talks-track-heading-color)}}
     track-name]
    [:div 
     (doall (map (fn [t]
                   ^{:key (:id t)}
                   [render-talk ctx conference t]) talks))]]])


(defn render [ctx]
  (let [current-route (route> ctx)
        form-props [:schedule (:id current-route)]
        form-data (:data @(forms-helpers/form-state ctx form-props))
        conference (or form-data (sub> ctx :current-schedule))
        track-count (js/parseInt (get-in conference [:confeedence-tags :track-count]))
        grouped-events (group-events (sub> ctx :current-schedule-events))
        timeline-events (prepare-timeline (concat (:event grouped-events) (:news grouped-events)))
        grouped-talks (group-talks-by-track (sort-events (:talk grouped-events)) track-count)]

    [main-wrap {:style {:background-color (get-color conference :main-bg-color)}}
     [conference-info-wrap
      [title-center {:style {:color (get-color conference :main-heading-color)}} (:name conference)]
      [conference-description {:style {:color (get-color conference :main-text-color)}} (:description conference)]
      [center-div
       [-action-link {:href (ui/url ctx (assoc current-route :form {:type "conference"}))} "Edit Conference Info"]]]
     [events-wrap {:style {:background-color (get-color conference :events-bg-color)}}
      [subtitle-center {:style {:color (get-color conference :events-heading-color)}} "Events"]
      [render-events ctx timeline-events]
      [center-div
       [-action-link {:href (ui/url ctx (assoc current-route :form {:type "event"})) :class "mr1"} "Add New event"]
       [-action-link {:href (ui/url ctx (assoc current-route :form {:type "news"})) :class "ml1"} "Add News"]]]
     [talks-wrap {:style {:background-color (get-color conference :talks-bg-color)}}
      [subtitle-center {:style {:color (get-color conference :talks-heading-color)}} "Talks"]
      [talks-column-wrap
       (doall (map (fn [idx]
                     ^{:key idx}
                     [render-talks ctx conference (str "Track #" (inc idx)) (get grouped-talks (inc idx))])
                   (range 0 track-count)))
       (when (seq (:unassigned grouped-talks))
         [render-talks ctx conference "Unassigned Talks" (:unassigned grouped-talks)])]
      [:div.center.pb2 [-action-link
                        {:href (ui/url ctx (assoc current-route :form {:type "talk"}))}
                        "Add New Talk"]]]]))

(def component
  (ui/constructor {:renderer render
                   :subscription-deps [:current-schedule :form-state :current-schedule-events]}))
