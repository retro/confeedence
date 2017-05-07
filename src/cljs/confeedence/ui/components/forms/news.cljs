(ns confeedence.ui.components.forms.news
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.css.core :refer-macros [defelement]]
            [confeedence.ui.components.pure.form-elements
             :refer [controlled-input
                     controlled-textarea
                     controlled-group-select
                     controlled-radio-group
                     controlled-time-picker
                     controlled-checkbox
                     -green-button]]
            [keechma.toolbox.forms.core :as forms-core]
            [keechma.toolbox.forms.helpers :as forms-helpers]
            [confeedence.forms.validators :as validators]
            [keechma.toolbox.ui :refer [route>]]
            [confeedence.static-data :refer [timezones]]
            [confeedence.ui.components.forms.event-shared :refer [time-period-opts get-date-format get-time-format]]))

(defelement -form-wrap
  :tag :form
  :class [:p2 :mx-auto :max-width-4 :flex-auto])

(defn render [ctx]
  (let [current-route (route> ctx)
        form-props [:news (or (get-in current-route [:form :id]) "new")]
        form-state @(forms-helpers/form-state ctx form-props)
        helpers (forms-helpers/make-component-helpers ctx form-props)
        submit (:submit helpers)
        new? (not (get-in form-state [:data :id]))]
    
    [-form-wrap {:on-submit submit}
     [:h1 (if new? "Create news" "Update news")]
     (when (= :submit-failed (get-in form-state [:state :type]))
       [:div.bg-red.c-white.p1.mb2 "We couldn't save the news item"])
     [controlled-input {:form-state form-state :helpers helpers :placeholder "Name" :attr :name}]
     [controlled-textarea {:form-state form-state :helpers helpers :placeholder "Description" :attr :description}]
     [:hr]
     
     [controlled-time-picker
      {:form-state form-state
       :helpers helpers
       :label "Publish date time"
       :attr :when.startDate
       :date-format (get-date-format form-state)
       :time-format (get-time-format form-state)}]
     [controlled-group-select {:form-state form-state :helpers helpers :placeholder "Timezone" :attr :when.startTimezone :options timezones}]
     
    
     [-green-button "Save Event"]]))

(def component
  (ui/constructor {:renderer render
                   :subscription-deps [:form-state]
                   :topic forms-core/id-key}))


