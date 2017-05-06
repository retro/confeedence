(ns confeedence.ui.components.forms.access-token
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.css.core :refer-macros [defelement]]
            [confeedence.ui.components.pure.form-elements :refer [controlled-input -green-button]]
            [keechma.toolbox.forms.core :as forms-core]
            [keechma.toolbox.forms.helpers :as forms-helpers]
            [confeedence.forms.validators :as validators]))

(defelement form-wrap
  :tag :form
  :class [:m4 :mx-auto :max-width-4 :flex-auto])

(defn render [ctx]
  (let [form-props [:access-token :form]
        form-state @(forms-helpers/form-state ctx form-props)
        helpers (forms-helpers/make-component-helpers ctx form-props)
        submit (:submit helpers)]
    
    [form-wrap {:on-submit submit}
     (when (= :submit-failed (get-in form-state [:state :type]))
       [:div.bg-red.c-white.p1.mb2 "Wrong Access Token"])
     [controlled-input {:form-state form-state :helpers helpers :placeholder "Access Token" :attr :access-token}]
     [-green-button "Save Access Token"]]))

(def component
  (ui/constructor {:renderer render
                   :subscription-deps [:form-state]
                   :topic forms-core/id-key}))


