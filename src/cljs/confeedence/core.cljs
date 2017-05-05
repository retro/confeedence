(ns confeedence.core
  (:require
   [reagent.core :as reagent]
   [keechma.app-state :as app-state]
   [confeedence.ui :refer [ui]]
   [confeedence.controllers :refer [controllers]]
   [confeedence.subscriptions :refer [subscriptions]]))


(def app-definition
  {:routes [["", {:page "homepage"}]
            ":page"
            ":page/:id"]
   :components    ui
   :controllers   controllers
   :subscriptions subscriptions 
   :html-element  (.getElementById js/document "app")})

(defonce running-app (clojure.core/atom))

(defn start-app! []
  (reset! running-app (app-state/start! app-definition)))

(defn dev-setup []
  (when ^boolean js/goog.DEBUG
    (enable-console-print!)
    (println "dev mode")))

(defn reload []
  (let [current @running-app]
    (if current
      (app-state/stop! current start-app!)
      (start-app!))))

(defn ^:export main []
  (dev-setup)
  (start-app!))
