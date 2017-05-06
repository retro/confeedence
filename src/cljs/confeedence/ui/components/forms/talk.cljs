(ns confeedence.ui.components.forms.talk
  (:require [keechma.ui-component :as ui]
            [confeedence.ui.components.pure.form-elements
             :refer [controlled-input
                     controlled-textarea
                     controlled-select
                     controlled-group-select
                     controlled-radio-group
                     controlled-time-picker
                     controlled-checkbox
                     -green-button]]
            [keechma.toolbox.forms.core :as forms-core]
            [keechma.toolbox.forms.helpers :as forms-helpers]
            [confeedence.forms.validators :as validators]
            [keechma.toolbox.ui :refer [route> sub>]]
            [confeedence.static-data :refer [timezones]]
            [confeedence.ui.components.forms.event-shared :refer [time-period-opts get-date-format get-time-format]]))


(defn render [ctx]
  (let [current-route (route> ctx)
        form-props [:talk (or (get-in current-route [:form :id]) "new")]
        form-state @(forms-helpers/form-state ctx form-props)
        helpers (forms-helpers/make-component-helpers ctx form-props)
        submit (:submit helpers)
        conference (sub> ctx :current-schedule)
        track-count (js/parseInt (get-in conference [:confeedence-tags :track-count]))]
    
    [:form.m4 {:on-submit submit}
     (when (= :submit-failed (get-in form-state [:state :type]))
       [:div.bg-red.c-white.p1.mb2 "We couldn't save the event"])
     [controlled-input {:form-state form-state :helpers helpers :placeholder "Talk Title" :attr :name}]
     [controlled-textarea {:form-state form-state :helpers helpers :placeholder "Description" :attr :confeedence.custom-fields.description}]
     [controlled-input {:form-state form-state :helpers helpers :placeholder "Speaker Name" :attr :confeedence.custom-fields.speaker-name}]
     [controlled-textarea {:form-state form-state :helpers helpers :placeholder "Speaker Bio" :attr :confeedence.custom-fields.speaker-bio}]
     [controlled-input {:form-state form-state :helpers helpers :placeholder "Speaker Photo URL" :attr :confeedence.custom-fields.speaker-photo-url}]
     [:hr]
     [controlled-select
      {:form-state form-state
       :helpers helpers
       :placeholder "Track"
       :attr :confeedence.custom-fields.track
       :options (concat [["" "Please Select"]] (map (fn [t] [(inc t) (inc t)]) (range 0 track-count)))}]
     [:hr]
     
     [controlled-time-picker
      {:form-state form-state
       :helpers helpers
       :label "Talk Start Date/Time"
       :attr :when.startDate
       :date-format (get-date-format form-state)
       :time-format (get-time-format form-state)}]

      [controlled-time-picker
      {:form-state form-state
       :helpers helpers
       :label "Talk End Date/Time"
       :attr :when.endDate
       :date-format (get-date-format form-state)
       :time-format (get-time-format form-state)}]

     [controlled-group-select {:form-state form-state :helpers helpers :placeholder "Timezone" :attr :when.startTimezone :options timezones}]
     
     
     [-green-button "Save Event"]]))

(def component
  (ui/constructor {:renderer render
                   :subscription-deps [:form-state :current-schedule]
                   :topic forms-core/id-key}))


