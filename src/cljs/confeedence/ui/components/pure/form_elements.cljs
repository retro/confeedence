(ns confeedence.ui.components.pure.form-elements
(:require [keechma.toolbox.css.core :refer-macros [defelement]]
            [confeedence.stylesheets.colors :refer [colors-with-variations]]
            [keechma.toolbox.forms.helpers :as forms-helpers]
            [confeedence.forms.validators :as validators]
            [reagent.core :as r]))

(defelement -input
  :tag :input
  :class [:bg-lightest-gray :ff-p1 :c-dark :p1]
  :style [{:width "100%"
           :border "none"
           :outline "none"
           :border-bottom (str "2px solid " (:lighter-gray colors-with-variations))}
          [:&:focus {:border-bottom-color (:green colors-with-variations)}]])

(defelement -textarea
  :tag :textarea
  :class [:bg-lightest-gray :ff-p1 :c-dark :p1]
  :style [{:width "100%"
           :border "none"
           :outline "none"
           :min-height "100px"
           :border-bottom (str "2px solid " (:lighter-gray colors-with-variations))}
          [:&:focus {:border-bottom-color (:green colors-with-variations)}]])

(defelement -select
  :tag :select
  :class [:ff-p1 :border :bd-lighter-gray :bg-lightest-gray]
  :style {:width "50%"})

(defelement -label
  :tag :label
  :class [:ff-h5 :c-gray :block :mb1])

(defelement -green-button
  :tag :button
  :style {:border-radius "2px"}
  :class [:bg-green :c-white :ff-p1 :btn :cursor-pointer :bg-h-green-hover :border-none :px2 :py1])

(defn render-errors [attr-errors]
  (when-let [errors (get-in attr-errors [:$errors$ :failed])]
    (into [:div.mt1]
          (doall (map (fn [e]
                        [:div.ff-p2.c-red (validators/get-validator-message e)])
                      errors)))))

(defn controlled-input [{:keys [form-state helpers placeholder label attr]}]
  (let [{:keys [on-change on-blur]} helpers]
    [:div.mb2
     [-label (or placeholder label)]
     [-input {:placeholder placeholder
              :on-change (on-change attr)
              :on-blur (on-blur attr)
              :value (forms-helpers/attr-get-in form-state attr)}]
     (render-errors (forms-helpers/attr-errors form-state attr))]))

(defn controlled-textarea [{:keys [form-state helpers placeholder label attr]}]
  (let [{:keys [on-change on-blur]} helpers]
    [:div.mb2
     [-label (or placeholder label)]
     [-textarea {:placeholder placeholder
                 :on-change (on-change attr)
                 :on-blur (on-blur attr)
                 :value (forms-helpers/attr-get-in form-state attr)}]
     (render-errors (forms-helpers/attr-errors form-state attr))]))

(defn controlled-select [{:keys [form-state stop-editing helpers placeholder label attr options]}]
  (let [on-change (:on-change helpers)
        value (forms-helpers/attr-get-in form-state (keyword attr))]
    [:div.mb2
     [-label (or placeholder label)]
     [-select {:on-change (on-change attr) :value (or value "")}
      (doall (map (fn [o] [:option {:value (first o) :key (first o)} (last o)]) options))]]))
