(ns confeedence.ui.components.schedule-list
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer (sub>)]))

(defn render [ctx]
  (let [schedules (sub> ctx :schedules)
        loading? (= :pending (:status (sub> ctx :schedules-meta)))]
    (when-not loading?
      (if (seq schedules)
        [:div
         [:div "Created Schedules"]
         [:ul
          (doall (map (fn [s]
                        [:li {:key (:id s)}
                         [:a {:href (ui/url ctx {:page "edit" :id (:id s)})}
                          (:name s)]])
                      schedules))]]
        [:div.bg-blue.c-white.p1 "There are no created schedules"]))))

(def component
  (ui/constructor {:renderer render
                   :subscription-deps [:schedules :schedules-meta]}))
