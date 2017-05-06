(ns confeedence.controllers.notifications
  (:require [keechma.toolbox.pipeline.core :as pp :refer-macros [pipeline!]]
            [keechma.toolbox.pipeline.controller :as pp-controller]
            [confeedence.edb :refer [append-collection remove-item]]
            [promesa.core :as p]))

(defn delay-pipeline []
  (p/promise (fn [resolve _] (js/setTimeout resolve 4000))))

(def controller
  (pp-controller/constructor
   (fn [_] true)
   {:add (pipeline! [value app-db]
           (assoc value :id (name (gensym "notification")))
           (pp/commit! (append-collection app-db :notification :list [value]))
           (delay-pipeline)
           (pp/commit! (remove-item app-db :notification (:id value))))
    :remove (pipeline! [value app-db]
              (pp/commit! (remove-item app-db :notification value)))}))
