(ns confeedence.forms
  (:require [confeedence.forms.access-token :as access-token]
            [confeedence.forms.schedule :as schedule]
            [confeedence.forms.event :as event]))

(def forms
  {:access-token (access-token/constructor)
   :schedule (schedule/constructor)
   :event (event/constructor)})

(def forms-params
  {:access-token (fn [{:keys [page]}]
                   (when (= "edit" page)
                     :form))

   :event (fn [{:keys [page id form]}]
            (when (and (= "edit" page)
                       (= "event" (:type form)))
              (or (:id form) "new")))

   :schedule (fn [{:keys [page id form]}]
               (cond
                 (and (nil? id) (= page "edit")) :new
                 (and id (= page "edit") (= "conference" (:type form))) id
                 :else nil))})

