(ns confeedence.ui.components.forms.access-token
  (:require [keechma.ui-component :as ui]
            [confeedence.ui.components.pure.form-elements :refer [controlled-input -green-button]]
            [keechma.toolbox.forms.core :as forms-core]
            [keechma.toolbox.forms.helpers :as forms-helpers]
            [confeedence.forms.validators :as validators]))

(defn render [ctx]
  (let [form-props [:access-token :form]
        form-state @(forms-helpers/form-state ctx form-props)
        helpers (forms-helpers/make-component-helpers ctx form-props)
        submit (:submit helpers)]
    
    [:form.m4 {:on-submit submit}
     (when (= :submit-failed (get-in form-state [:state :type]))
       [:div.bg-red.c-white.p1.mb2 "Wrong Access Token"])
     [controlled-input {:form-state form-state :helpers helpers :placeholder "Access Token" :attr :access-token}]
     [-green-button "Save Access Token"]]))

(def component
  (ui/constructor {:renderer render
                   :subscription-deps [:form-state]
                   :topic forms-core/id-key}))


