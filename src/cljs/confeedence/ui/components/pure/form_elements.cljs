(ns confeedence.ui.components.pure.form-elements
(:require [keechma.toolbox.css.core :refer-macros [defelement]]
            [confeedence.stylesheets.colors :refer [colors-with-variations]]
            [keechma.toolbox.forms.helpers :as forms-helpers]
            [confeedence.forms.validators :as validators]
            [reagent.core :as r]
            [cljsjs.react-datetime]))

(defelement -input
  :tag :input
  :class [:bg-lightest-gray :c-small :c-dark :p1]
  :style [{:width "100%"
           :border "none"
           :outline "none"
           :border-bottom (str "2px solid " (:lighter-gray colors-with-variations))}
          [:&:focus {:border-bottom-color (:green colors-with-variations)}]])

(defelement -datetime-input-wrap
  :tag :div
  :style [[:.form-control {:width "100%"
           :border "none"
           :outline "none"
           :border-bottom (str "2px solid " (:lighter-gray colors-with-variations))}
           [:&:focus {:border-bottom-color (:green colors-with-variations)}]]])

(defelement -textarea
  :tag :textarea
  :class [:bg-lightest-gray :c-small :c-dark :p1]
  :style [{:width "100%"
           :border "none"
           :outline "none"
           :min-height "100px"
           :border-bottom (str "2px solid " (:lighter-gray colors-with-variations))}
          [:&:focus {:border-bottom-color (:green colors-with-variations)}]])

(defelement -select
  :tag :select
  :class [:c-small :border :bd-lighter-gray :bg-lightest-gray]
  :style {:width "50%"})

(defelement -label
  :tag :label
  :class [:c-medium :c-gray :block :mb1])

(defelement -faux-label
  :tag :span
  :class [:c-medium :c-gray :block :mb1])

(defelement -green-button
  :tag :button
  :style {:border-radius "2px"}
  :class [:bg-green :c-white :c-button :btn :cursor-pointer :bg-h-green-hover :border-none :px2 :py1 :mx-auto :flex])

(defn render-errors [attr-errors]
  (when-let [errors (get-in attr-errors [:$errors$ :failed])]
    (into [:div.mt1]
          (doall (map (fn [e]
                        [:div.c-medium.c-red (validators/get-validator-message e)])
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

(defn controlled-select [{:keys [form-state helpers placeholder label attr options]}]
  (let [on-change (:on-change helpers)
        value (forms-helpers/attr-get-in form-state (keyword attr))]
    [:div.mb2
     [-label (or placeholder label)]
     [-select {:on-change (on-change attr) :value (or value "")}
      (doall (map (fn [o] [:option {:value (first o) :key (first o)} (last o)]) options))]]))

(defn controlled-time-picker  [{:keys [form-state helpers placeholder label attr options time-format date-format]}]
  (let [{:keys [set-value]} helpers]
    [:div.mb2
     [-label (or placeholder label)]
     [-datetime-input-wrap {:key [time-format date-format]}
      [:> js/Datetime {:value (forms-helpers/attr-get-in form-state attr)
                       :on-change #(set-value attr %)
                       :time-format time-format
                       :date-format date-format
                       :input-props {:class-name "form-control bg-lightest-gray c-small c-dark p1"}}]] 
     (render-errors (forms-helpers/attr-errors form-state attr))]))

(defn controlled-radio-group [{:keys [form-state helpers placeholder label attr options]}]
  (let [on-change (:on-change helpers)
        value (forms-helpers/attr-get-in form-state (keyword attr))]
    [:div.mb2
     [-faux-label (or placeholder label)]
     (doall
      (map (fn [[opt-value label]]
             [:label.inline-block.mr2 {:key opt-value}
              [:input.mr1
               {:type :radio :value opt-value :checked (= opt-value value) :on-change (on-change attr)}]
              label])
           options))]))

(defn controlled-checkbox [{:keys [form-state helpers placeholder label attr]}]
  (let [{:keys [set-value]} helpers
        value (forms-helpers/attr-get-in form-state attr)]
    [:div.mb2
     [:label.block
      [:input.mr1 {:type :checkbox
                   :on-change #(set-value attr (not value))
                   :checked (boolean value)
                   :value true}]
      (or placeholder label)]
     (render-errors (forms-helpers/attr-errors form-state attr))]))

(defn controlled-group-select [{:keys [form-state helpers placeholder label attr options]}]
  (let [on-change (:on-change helpers)
        value (forms-helpers/attr-get-in form-state (keyword attr))]
    [:div.mb2
     [-label (or placeholder label)]
     [-select {:on-change (on-change attr) :value (or value "")}
      (doall (map (fn [optgroup]
                    (let [label (first optgroup)
                          opts (rest optgroup)]
                      [:optgroup {:key label :label label}
                       (doall (map (fn [o]
                                     [:option {:value (first o) :key (first o)} (last o)]) opts))])) options))]]))
