(ns confeedence.subscriptions
  (:require [confeedence.util.dataloader :refer [make-subscriptions]]
            [confeedence.datasources :refer [datasources]]
            [confeedence.edb :as edb :refer [edb-schema]]
            [keechma.toolbox.forms.helpers :as forms-helpers])
  (:require-macros [reagent.ratom :refer [reaction]]))

(defn get-kv [key]
  (fn [app-db-atom]
    (reaction
     (get-in @app-db-atom (flatten [:kv key])))))

(defn get-collection [app-db type name]
  (edb/get-collection app-db type name))

(defn get-named-item [app-db type name]
  (edb/get-named-item app-db type name))

(defn list-for
  ([type] (list-for type :list))
  ([type list-name]
   (fn [app-db-atom]
     (reaction
      (get-collection @app-db-atom type list-name)))))

(defn named-item-for
  ([type] (named-item-for type :current))
  ([type named-item]
   (fn [app-db-atom]
     (reaction
(get-named-item @app-db-atom type named-item)))))

(def subscriptions
  (merge
   (make-subscriptions datasources edb-schema)
   {:form-state forms-helpers/form-state-sub
    :notifications (list-for :notification)}))
