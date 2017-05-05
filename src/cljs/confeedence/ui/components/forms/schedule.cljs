(ns confeedence.ui.components.forms.schedule
  (:require [keechma.ui-component :as ui]
            [confeedence.ui.components.pure.form-elements :refer
             [controlled-input controlled-select controlled-textarea  -green-button]]
            [keechma.toolbox.forms.core :as forms-core]
            [keechma.toolbox.forms.helpers :as forms-helpers]
            [confeedence.forms.validators :as validators]
            [keechma.toolbox.ui :refer [route>]]))

(defn render [ctx]
  (let [schedule-id (or (:id (route> ctx)) :new)
        form-props [:schedule schedule-id]
        form-state @(forms-helpers/form-state ctx form-props)
        helpers (forms-helpers/make-component-helpers ctx form-props)
        submit (:submit helpers)]
    
    [:form.m4 {:on-submit submit}
     [:h1 "Create a new conference"]
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
     [-green-button "Save Conference"]]))

(def component
  (ui/constructor {:renderer render
                   :subscription-deps [:form-state]
                   :topic forms-core/id-key}))


