(ns confeedence.forms.event
  (:require [keechma.toolbox.forms.core :as forms-core]
            [forms.validator :as v]
            [confeedence.forms.validators :as validators]
            [keechma.toolbox.pipeline.core :as pp]
            [keechma.toolbox.forms.core :as forms-core]
            [keechma.toolbox.forms.helpers :as forms-helpers]
            [confeedence.util.whenhub-api :refer [create-event]]
            [keechma.toolbox.pipeline.core :as pp :refer-macros [pipeline!]]
            [promesa.core :as p]
            [keechma.toolbox.dataloader.core :as dataloader]
            [keechma.toolbox.dataloader.core :as dataloader]
            [cljsjs.moment-timezone]
            [medley.core :refer [dissoc-in]]
            [confeedence.util :refer [get-access-token]]))

(def validator (v/validator {:name [[:not-empty validators/not-empty?]]}))

(defn process-has-end-date [app-db form-props form-state path value]
  (if value
    (-> form-state
        (assoc-in [:data :when :endDate] (.now js/moment))
        (assoc-in [:data :when :endTimezone] (get-in form-state [:data :when :startTimezone]))
        (assoc-in [:data :confeedence :has-end-date] value))
    (-> form-state
        (dissoc-in [:data :when :endDate])
        (dissoc-in [:data :when :endTimezone])
        (assoc-in [:data :confeedence :has-end-date] value))))

(def out-date-formats
  {"minute" "YYYY-MM-DDTHH:mm:ssZ"
   "day"    "YYYY-MM-DD"
   "year"   "YYYY"})

(defn process-date-out [data path timezone-path]
  (let [period (get-in data [:when :period])
        timezone (get-in data timezone-path)
        date (get-in data path)
        format (get out-date-formats period)]
    (if date
      (let [tz-date (.tz js/moment (.format (js/moment date) "YYYY-MM-DD HH:mm") timezone)]
        (assoc-in data path (.format tz-date format)))
      data)))

(defn process-custom-fields-out [data]
  (let [custom-fields (get-in data [:confeedence :custom-fields])]
    (assoc data :customFieldData
           {"3c8d41e5-90a9-43c4-b224-5b7efd43b1b8"
            (reduce (fn [acc [k v]] (conj acc {:label k :value v})) [] custom-fields)})))

(defrecord EventForm [validator]
  forms-core/IForm
  (process-out [this app-db form-props data]
    (-> data
        process-custom-fields-out
        (process-date-out [:when :startDate] [:when :startTimezone])
        (process-date-out [:when :endDate] [:when :endTimezone])
        (dissoc :confeedence)))
  (submit-data [_ app-db _ data]
    (let [schedule-id (get-in app-db [:route :data :id])]
      (create-event (get-access-token app-db) schedule-id data)))
  (process-attr-with [_ path]
    (when (= path [:confeedence :has-end-date])
      process-has-end-date))
  (get-data [this app-db form-props]
    (let [id (last form-props)]
      (if (= "new" id)
        {:when {:period "minute"
                :startDate (.now js/moment)
                :startTimezone "US/Central"}
         :confeedence {:has-end-date false
                       :custom-fields {:type "event"}}})))
  (on-submit-success [this app-db form-props data]
    (println "SUBMIT SUCCESS" data)))

(defn constructor []
  (->EventForm validator))

