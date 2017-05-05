(ns confeedence.util.whenhub-api
  (:require [keechma.toolbox.ajax :refer [GET POST PUT]]
            [promesa.core :as p]
            [clojure.string :as str]))

(defn process-schedule-tags [schedule]
  (let [tags (:tags schedule)
        processed (reduce (fn [acc tag]
                            (if (str/starts-with? tag "confeedence:")
                              (let [[key val] (rest (str/split tag #":"))]
                                (if (= "conference" key)
                                  acc
                                  (assoc-in acc [:confeedence-tags (keyword key)] val)))
                              (let [tags (:tags acc)]
                                (assoc acc :tags (conj tags tag)))))
                          {:confeedence-tags {} :tags []} tags)]
    (merge schedule processed)))

(defn load-user-info [access-token]
  (GET "https://api.whenhub.com/api/users/me"
       {:params {:access_token access-token}
        :format :json
        :response-format :json
        :keywords? true}))

(defn load-schedules [access-token]
  (let [filter {:where {:tags {:eq "confeedence:conference"}}}]
    (->> (GET "https://api.whenhub.com/api/users/me/schedules"
              {:params {:access_token access-token
                        :filter (.stringify js/JSON (clj->js filter))}
               :format :json
               :response-format :json
               :keywords? true})
         (p/map (fn [schedules] (map process-schedule-tags schedules))))))

(defn load-schedule-by-id [access-token id]
  (->> (GET (str "https://api.whenhub.com/api/users/me/schedules/" id)
            {:params {:access_token access-token}
             :format :json
             :response-format :json
             :keywords? true})
       (p/map process-schedule-tags)))

(defn create-schedule [access-token data]
  (->> (POST (str "https://api.whenhub.com/api/users/me/schedules?access_token=" access-token)
             {:params data
              :format :json
              :response-format :json
              :keywords? true})
       (p/map process-schedule-tags)))

(defn update-schedule [access-token data]
  (let [id (:id data)]
    (->> (PUT (str "https://api.whenhub.com/api/users/me/schedules/" id "?access_token=" access-token)
              {:params data
               :format :json
               :response-format :json
               :keywords? true})
         (p/map process-schedule-tags))))
