(ns confeedence.edb
  (:require-macros [keechma.toolbox.edb :refer [defentitydb]]))

(def edb-schema
  {:schedule {:id :id}})

(defentitydb edb-schema)
