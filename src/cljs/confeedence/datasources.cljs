(ns confeedence.datasources
  (:require [hodgepodge.core :refer [get-item local-storage]]
            [confeedence.util.dataloader :refer [map-loader reload-params]]
            [confeedence.util.whenhub-api :refer [load-user-info load-schedules load-schedule-by-id]]))

(def access-token-loader 
  (map-loader
   (fn [_]
     (get-item local-storage "whenhub-access-token"))))

(def ignore-datasource
  :keechma.toolbox.dataloader.core/ignore)

(def datasources
  {:access-token {:target [:kv :access-token]
                  :loader access-token-loader
                  :params (fn [prev _ _]
                            (when prev
                              ignore-datasource))}

   :schedules {:target [:edb/collection :schedule/list]
               :deps [:access-token]
               :params (fn [prev route deps]
                         (when (and (= "edit" (:page route))
                                    (nil? (:id route)))
                           deps))
               :loader (map-loader (fn [params]
                                     (when-let [access-token (get-in params [:params :access-token])]
                                       (load-schedules access-token))))}

   :current-schedule {:target [:edb/named-item :schedule/current]
                      :deps [:access-token]
                      :loader (map-loader (fn [params]
                                            (when-let [access-token (get-in params [:params :access-token])]
                                              (load-schedule-by-id access-token (get-in params [:params :id])))))
                      :params (fn [prev route {:keys [access-token]}]
                                (let [page (:page route)
                                      id (:id route)
                                      prev-schedule (:data prev)] 
                                  (when (and (or (= "show" page)
                                                 (= "edit" page))
                                             id)
                                    (if (= id (:id prev-schedule))
                                      ignore-datasource
                                      {:access-token access-token
                                       :id id}))))}

   :current-user {:target [:kv :current-user]
                  :deps [:access-token]
                  :loader (map-loader
                           (fn [loader-params]
                             (let [access-token (get-in loader-params [:params :access-token])]
                               (when access-token
                                 (load-user-info access-token)))))
                  :params (fn [prev route {:keys [access-token]}]
                            (if (:data prev)
                              ignore-datasource
                              {:access-token access-token}))}})
