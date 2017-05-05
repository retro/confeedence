(ns confeedence.util.dataloader
  (:require [keechma.toolbox.dataloader.core :refer [get-data get-meta]])
  (:require-macros [reagent.ratom :refer [reaction]]))

(defn map-loader [loader]
  (fn [params]
    (map loader params)))

(defn reload-params [_ _ _]
  (name (gensym "reload-dataloader")))

(defn make-subscriptions [datasources edb-schema]
  (reduce (fn [acc [datasource-key datasource]]
            (-> acc
                (assoc (keyword (str (name datasource-key) "-meta"))
                       (fn [app-db-atom]
                         (reaction
                          (let [app-db @app-db-atom]
                            (get-meta app-db datasource-key)))))
                (assoc datasource-key
                       (fn [app-db-atom]
                         (reaction
                          (let [app-db @app-db-atom]
                            (get-data app-db edb-schema (:target datasource)))))))) {} datasources))

(defn existing-datasource [target]
  {:target target
   :params (fn [prev _ _]
             (:data prev))
   :loader (map-loader (fn [{:keys [params]}] params))})
