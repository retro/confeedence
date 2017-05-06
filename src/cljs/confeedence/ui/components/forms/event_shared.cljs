(ns confeedence.ui.components.forms.event-shared)

(def time-period-opts
  [["minute" "Minute"]
   ["day" "Day"]
   ["month" "Month"]])

(defn get-date-format [form-state]
  (let [period (get-in form-state [:data :when :period])]
    (case period
      "minute" "MMMM Do YYYY,"
      "day" "MMMM Do YYYY"
      "month" "MMMM YYYY"
      "YYYY")))

(defn get-time-format [form-state]
  (let [period (get-in form-state [:data :when :period])]
    (when (= "minute" period)
      "hh:mm a")))
