(ns confeedence.ui.components.forms.schedule
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.css.core :refer-macros [defelement]]
            [confeedence.ui.components.pure.form-elements :refer
             [controlled-input controlled-select controlled-textarea  -green-button]]
            [keechma.toolbox.forms.core :as forms-core]
            [keechma.toolbox.forms.helpers :as forms-helpers]
            [confeedence.forms.validators :as validators]
            [keechma.toolbox.ui :refer [route>]]
            [confeedence.stylesheets.colors :refer [theme-colors]]))

(defelement form-wrap
  :tag :form
  :class [:p2 :mx-auto :max-width-4 :flex-auto])

(def theme-color-options
  (sort-by first (map (fn [c] [(:slug c) (:name c)]) theme-colors)))

(def color-options
  {:main {:main-heading-color "Main Heading Color"
          :main-text-color "Main Text Color"
          :main-bg-color "Main Background Color"}
   :events {:events-heading-color "Events Heading Color"
            :events-bg-color "Events Background Color"
            :events-timeline-bg-color "Events - Timeline Background Color"
            :events-timeline-text-color "Events - Timeline Text Color"
            :events-callout-bg-color "Events - Callout Background Color"
            :events-callout-heading-color "Events - Callout Heading Color"
            :events-callout-text-color "Events - Callout Text Color"}
   :talks {:talks-heading-color "Talks Heading Color"
           :talks-bg-color "Talks Background Color"
           :talks-track-bg-color "Talks - Track Background Color"
           :talks-track-heading-color "Talks - Track Heading Color"
           :talks-talk-heading-color "Talks - Talk Heading Color"
           :talks-talk-text-color "Talks - Talk Text Color"}})

(defn render-color-selects [form-state helpers options]
  (into [:div]
        (doall (map (fn [[attr label]]
                      [controlled-select
                       {:form-state form-state
                        :helpers helpers
                        :placeholder label
                        :attr (str "confeedence-tags." (name attr))
                        :options theme-color-options}]) options))))

(defn render [ctx]
  (let [schedule-id (or (:id (route> ctx)) :new)
        form-props [:schedule schedule-id]
        form-state @(forms-helpers/form-state ctx form-props)
        helpers (forms-helpers/make-component-helpers ctx form-props)
        submit (:submit helpers)
        new? (not (get-in form-state [:data :id]))]
   
    [form-wrap {:on-submit submit}
     [:h1 (if new? "Create a new conference" "Update conference")]
     (when (= :submit-failed (get-in form-state [:state :type]))
       [:div.bg-red.c-white.p1.mb2 "We couldn't save the conference"])
     [controlled-input {:form-state form-state :helpers helpers :placeholder "Name" :attr :name}]
     [controlled-textarea {:form-state form-state :helpers helpers :placeholder "Description" :attr :description}]
     [controlled-select
      {:form-state form-state
       :helpers helpers
       :placeholder "Number of Tracks"
       :attr :confeedence-tags.track-count
       :options (map (fn [v] [v v]) ["1" "2" "3" "4" "5"])}]
     (when (not new?)
       [:div
        [render-color-selects form-state helpers (:main color-options)]
        [:hr]
        [render-color-selects form-state helpers (:events color-options)]
        [:hr]
        [render-color-selects form-state helpers (:talks color-options)]])
     [-green-button "Save Conference"]]))

(def component
  (ui/constructor {:renderer render
                   :subscription-deps [:form-state]
                   :topic forms-core/id-key}))


