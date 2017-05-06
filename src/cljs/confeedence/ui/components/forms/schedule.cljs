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
        [controlled-select
         {:form-state form-state
          :helpers helpers
          :placeholder "Main Heading Color"
          :attr :confeedence-tags.main-heading-color
          :options theme-color-options}]
        [controlled-select
         {:form-state form-state
          :helpers helpers
          :placeholder "Main Text Color"
          :attr :confeedence-tags.main-text-color
          :options theme-color-options}]
        [controlled-select
         {:form-state form-state
          :helpers helpers
          :placeholder "Main Background Color"
          :attr :confeedence-tags.main-bg-color
          :options theme-color-options}]
        [:hr]
        [controlled-select
         {:form-state form-state
          :helpers helpers
          :placeholder "Events Heading Color"
          :attr :confeedence-tags.events-heading-color
          :options theme-color-options}]
        [controlled-select
         {:form-state form-state
          :helpers helpers
          :placeholder "Events Background Color"
          :attr :confeedence-tags.events-bg-color
          :options theme-color-options}]
        [:hr]
        [controlled-select
         {:form-state form-state
          :helpers helpers
          :placeholder "Talks Heading Color"
          :attr :confeedence-tags.talks-heading-color
          :options theme-color-options}]
        [controlled-select
         {:form-state form-state
          :helpers helpers
          :placeholder "Talks Background Color"
          :attr :confeedence-tags.talks-bg-color
          :options theme-color-options}]
         [controlled-select
         {:form-state form-state
          :helpers helpers
          :placeholder "Talks - Track Heading Color"
          :attr :confeedence-tags.talks-track-heading-color
          :options theme-color-options}]
        [controlled-select
         {:form-state form-state
          :helpers helpers
          :placeholder "Talks - Track Background Color"
          :attr :confeedence-tags.talks-track-bg-color
          :options theme-color-options}]])
     [-green-button "Save Conference"]]))

(def component
  (ui/constructor {:renderer render
                   :subscription-deps [:form-state]
                   :topic forms-core/id-key}))


