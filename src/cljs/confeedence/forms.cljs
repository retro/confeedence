(ns confeedence.forms
  (:require [confeedence.forms.access-token :as access-token]
            [confeedence.forms.schedule :as schedule]))

(def forms
  {:access-token (access-token/constructor)
   :schedule (schedule/constructor)})

(def forms-params
  {:access-token (fn [{:keys [page]}]
                   (when (= "edit" page)
                     :form))
   :schedule (fn [{:keys [page id form]}]
               (cond
                 (and (nil? id) (= page "edit")) :new
                 (and id (= page "edit") (= "conference" (:type form))) id
                 :else nil))})

