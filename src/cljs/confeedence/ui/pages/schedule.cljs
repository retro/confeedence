(ns confeedence.ui.pages.schedule
  (:require [keechma.ui-component :as ui]))

(defn render [ctx]
  [(ui/component ctx :schedule)])

(def component
  (ui/constructor {:renderer render
                   :component-deps [:schedule]}))
