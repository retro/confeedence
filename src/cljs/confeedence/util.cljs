(ns confeedence.util)

(defn get-access-token [app-db]
  (get-in app-db [:kv :access-token]))
