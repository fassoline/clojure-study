(ns cash-control.db)

(def database (atom []))
(defn transactions [] @database)

(defn save
  [transaction]
  (let [updated-database (swap! database conj transaction)]
    (merge transaction {:id (count updated-database)})))

(defn clear-db []
  (reset! database []))