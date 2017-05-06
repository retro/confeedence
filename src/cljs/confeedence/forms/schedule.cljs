(ns confeedence.forms.schedule
  (:require [keechma.toolbox.forms.core :as forms-core]
            [forms.validator :as v]
            [confeedence.forms.validators :as validators]
            [keechma.toolbox.pipeline.core :as pp :refer-macros [pipeline!]]
            [keechma.toolbox.forms.core :as forms-core]
            [keechma.toolbox.forms.helpers :as forms-helpers]
            [confeedence.util.whenhub-api :refer [create-schedule update-schedule]]
            [promesa.core :as p]
            [hodgepodge.core :refer [set-item local-storage]]
            [keechma.toolbox.dataloader.core :as dataloader]
            [keechma.toolbox.dataloader.controller :as dataloader-controller]
            [confeedence.edb :refer [get-named-item append-collection insert-named-item]]
            [confeedence.util :refer [get-access-token]]))

(def validator (v/validator {:name [[:not-empty validators/not-empty?]]}))

(defn prepare-tags [data]
  (let [tags (:confeedence-tags data)]
    (assoc data :tags
           (concat (:tags data)
                   (reduce (fn [acc [key value]] (conj acc (str "confeedence:" (name key) ":" value)))
                           ["confeedence:conference"] tags)))))

(defn add-custom-field [data]
  (assoc data :customFields
         [{:id "3c8d41e5-90a9-43c4-b224-5b7efd43b1b8" ;; magical constant for label / value field
           :customField "label-value-fields"
           :name "Label/Value Fields"
           :config {:fieldName "custom"}}]))

(defn add-default-colors [data]
  (let [tags (:tags data)]
    (assoc data :tags
           (concat tags ["confeedence:main-heading-color:alizarin"
                         "confeedence:main-text-color:asbestos"
                         "confeedence:main-bg-color:dark-gray"

                         "confeedence:events-heading-color:white"
                         "confeedence:events-bg-color:belize-hole"
                         "confeedence:events-timeline-bg-color:pomegranate"
                         "confeedence:events-timeline-text-color:white"
                         "confeedence:events-callout-bg-color:midnight-blue"
                         "confeedence:events-callout-heading-color:alizarin"
                         "confeedence:events-callout-text-color:white"

                         "confeedence:talks-heading-color:white"
                         "confeedence:talks-bg-color:alizarin"
                         "confeedence:talks-track-bg-color:dark-gray"
                         "confeedence:talks-track-heading-color:alizarin"
                         "confeedence:talks-talk-heading-color:belize-hole"
                         "confeedence:talks-talk-text-color:white"]))))

(defn add-calendar [data]
  (assoc data :calender {:calendarType "absolute"}))

(defrecord ScheduleForm [validator]
  forms-core/IForm
  (get-data [this app-db form-props]
    (let [id (last form-props)]
      (if (= :new id)
        {:confeedence-tags {:track-count "1"}}
        (pipeline! [value app-db]
          (dataloader-controller/wait-dataloader-pipeline!)
          (get-named-item app-db :schedule :current)))))
  (submit-data [_ app-db _ data]
    (let [new? (not (:id data))
          processed-data (-> data
                             prepare-tags
                             add-custom-field
                             add-calendar)]
      (if new?
        (create-schedule (get-access-token app-db) (add-default-colors processed-data))
        (update-schedule (get-access-token app-db) processed-data))))
  (on-submit-success [this app-db form-props data]
    (let [new? (not= (:id data) (get-in app-db [:route :data :id]))]
      (if new?
        (pipeline! [value app-db]
          (pp/commit! (append-collection app-db :schedule :list [data]))
          (pp/redirect! {:page "edit" :id (:id data)})
          (pp/send-command! [:notifications :add] {:message "Conference was successfully created"}))
        (pipeline! [value app-db]
          (pp/commit! (insert-named-item app-db :schedule :current data))
          (pp/redirect! (dissoc (get-in app-db [:route :data]) :form))
          (pp/send-command! [:notifications :add] {:message "Conference settings saved"}))))))

(defn constructor []
  (->ScheduleForm validator))

