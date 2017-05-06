(ns confeedence.forms.event
  (:require [keechma.toolbox.forms.core :as forms-core]
            [forms.validator :as v]
            [confeedence.forms.validators :as validators]
            [keechma.toolbox.pipeline.core :as pp]
            [keechma.toolbox.forms.core :as forms-core]
            [keechma.toolbox.forms.helpers :as forms-helpers]
            [confeedence.util.whenhub-api :refer [create-event update-event]]
            [keechma.toolbox.pipeline.core :as pp :refer-macros [pipeline!]]
            [promesa.core :as p]
            [confeedence.edb :refer [get-collection append-collection insert-item get-item-by-id]]
            [keechma.toolbox.dataloader.core :as dataloader]
            [keechma.toolbox.dataloader.controller :as dataloader-controller]
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

(def type->name
  {"event" "Event"
   "news" "News item"
   "talk" "Talk"})

(defn build-success-message [data]
  (let [type (get-in data [:confeedence :custom-fields :type])]
    (str (get type->name type) " " (:name data) " was successfully saved.")))

(defn process-custom-fields-out [data]
  (let [custom-fields (get-in data [:confeedence :custom-fields])]
    (assoc data :customFieldData
           {"3c8d41e5-90a9-43c4-b224-5b7efd43b1b8"
            (reduce (fn [acc [k v]] (conj acc {:label (name k) :value v})) [] custom-fields)})))

(defn get-event-pipeline [id]
  (pipeline! [value app-db]
    (dataloader-controller/wait-dataloader-pipeline!)
    (get-item-by-id app-db :event id)))

(defn event-process-out [event-record app-db form-props data]
  (-> data
      process-custom-fields-out
      (process-date-out [:when :startDate] [:when :startTimezone])
      (process-date-out [:when :endDate] [:when :endTimezone])
      (dissoc :confeedence)))

(defn event-submit-data [event-record app-db form-props data]
  (let [schedule-id (get-in app-db [:route :data :id])
        new? (not (boolean (:id data)))]
    (if new?
      (create-event (get-access-token app-db) schedule-id data)
      (update-event (get-access-token app-db) schedule-id data))))

(defn event-process-attr-with [event-record path]
  (when (= path [:confeedence :has-end-date])
    process-has-end-date))

(defn talk-process-attr-with [event-record path]
  (when (or (= path [:when :startTimezone])
            (= path [:when :endTimezone]))
    (fn [app-db form-props form-state path value]
      (-> form-state
          (assoc-in [:data :when :startTimezone] value)
          (assoc-in [:data :when :endTimezone] value)))))

(defn event-get-data [event-record app-db form-props]
  (let [id (last form-props)]
    (if (= "new" id)
      {:when {:period "minute"
              :startDate (.now js/moment)
              :startTimezone "US/Central"}
       :confeedence {:has-end-date false
                     :custom-fields {:type "event"}}}
      (get-event-pipeline id))))

(defn event-on-submit-success [event-record app-db form-props data]
  (let [event-id (:id data)
        events (get-collection app-db :event :current-schedule-events)
        event-ids (set (map :id events))
        new? (not (contains? event-ids event-id))]
    (pipeline! [value app-db]
      (pp/commit! (if new?
                    (append-collection app-db :event :current-schedule-events [data])
                    (insert-item app-db :event data)))
      (pp/redirect! (dissoc (get-in app-db [:route :data]) :form))
      (pp/send-command! [:notifications :add] {:message (build-success-message data)}))))


(defn news-get-data [event-record app-db form-props]
  (let [id (last form-props)]
    (if (= "new" id)
      {:when {:period "minute"
              :startDate (.now js/moment)
              :startTimezone "US/Central"}
       :confeedence {:has-end-date false
                     :custom-fields {:type "news"}}}
      (get-event-pipeline id))))

(defn talk-get-data [event-record app-db form-props]
  (let [id (last form-props)]
    (if (= "new" id)
      {:when {:period "minute"
              :startDate (.now js/moment)
              :startTimezone "US/Central"
              :endDate (.now js/moment)
              :endTimezone "US/Central"}
       :confeedence {:has-end-date true
                     :custom-fields {:type "talk" :track "1"}}}
      (get-event-pipeline id))))

(defrecord EventForm [validator]
  forms-core/IForm
  (process-out [this app-db form-props data]
    (event-process-out this app-db form-props data))
  (submit-data [this app-db form-props data]
    (event-submit-data this app-db form-props data))
  (process-attr-with [this path]
    (event-process-attr-with this path))
  (get-data [this app-db form-props]
    (event-get-data this app-db form-props))
  (on-submit-success [this app-db form-props data]
    (event-on-submit-success this app-db form-props data)))

(defrecord NewsForm [validator]
  forms-core/IForm
  (process-out [this app-db form-props data]
    (event-process-out this app-db form-props data))
  (submit-data [this app-db form-props data]
    (event-submit-data this app-db form-props data))
  (process-attr-with [this path]
    (event-process-attr-with this path))
  (get-data [this app-db form-props]
    (news-get-data this app-db form-props))
  (on-submit-success [this app-db form-props data]
    (event-on-submit-success this app-db form-props data)))

(defrecord TalkForm [validator]
  forms-core/IForm
  (process-out [this app-db form-props data]
    (event-process-out this app-db form-props data))
  (submit-data [this app-db form-props data]
    (event-submit-data this app-db form-props data))
  (process-attr-with [this path]
    (talk-process-attr-with this path))
  (get-data [this app-db form-props]
    (talk-get-data this app-db form-props))
  (on-submit-success [this app-db form-props data]
    (event-on-submit-success this app-db form-props data)))


(defn event-constructor []
  (->EventForm validator))

(defn news-constructor []
  (->NewsForm validator))

(defn talk-constructor []
  (->TalkForm validator))
