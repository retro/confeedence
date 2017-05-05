(ns confeedence.forms.access-token
  (:require [keechma.toolbox.forms.core :as forms-core]
            [forms.validator :as v]
            [confeedence.forms.validators :as validators]
            [keechma.toolbox.pipeline.core :as pp]
            [keechma.toolbox.forms.core :as forms-core]
            [keechma.toolbox.forms.helpers :as forms-helpers]
            [confeedence.util.whenhub-api :refer [load-user-info]]
            [promesa.core :as p]
            [hodgepodge.core :refer [set-item local-storage]]
            [keechma.toolbox.dataloader.core :as dataloader]))

(def validator (v/validator {:access-token [[:not-empty validators/not-empty?]]}))

(defrecord AccessTokenForm [validator]
  forms-core/IForm
  (submit-data [_ app-db _ data]
    (let [access-token (:access-token data)]
      (->> (load-user-info (:access-token data))
           (p/map (fn [user-info]
                    {:access-token access-token
                     :current-user user-info})))))
  (on-submit-success [this app-db form-props data]
    (set-item local-storage "whenhub-access-token" (:access-token data))
    (pp/do!
     (pp/commit! (-> app-db
                     (assoc-in [:kv :current-user] (:current-user data))
                     (assoc-in [:kv :access-token] (:access-token data))))
     (pp/send-command! [dataloader/id-key :load-data] true))))

(defn constructor []
  (->AccessTokenForm validator))

