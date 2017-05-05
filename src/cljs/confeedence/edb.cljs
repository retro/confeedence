(ns confeedence.edb
  (:require-macros [keechma.toolbox.edb :refer [defentitydb]]))

(def edb-schema
  {})

(defentitydb edb-schema)
