(ns conversor.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clj-http.client :as http-client]
            [cheshire.core :as json])
  (:gen-class))

(def app-args
  [["-f" "--from base currency" "base currency"]
   ["-t", "--to destination currency" "destination currency"]])

(def api-key "")
(def api-url "https://free.currencyconverterapi.com/api/v6/convert")

(defn create-currency-parameter
  [from to]
  (str from "_" to))

(defn create-api-query-string
  [from to]
  (hash-map :query-params {"q" (create-currency-parameter from to) "apiKey" api-key}))

(defn get-current-quotation
  [from to]
  (-> (http-client/get api-url (create-api-query-string from to))
      (:body)
      (json/parse-string)
      (get-in ["results", "USD_BRL", "val"])))

(defn create-result-phrase
  [quotation from to]
  (str "1 " from " equivale a " quotation " em " to))

(defn -main
  [& args]
  (let [{:keys [from to]} (:options (parse-opts args app-args))]
    (-> (get-current-quotation from to)
        (create-result-phrase from to)
        (println))))

(-main "--from=USD" "--to=BRL")

; (def full-name {:name "Felipe" :last-name "Assoline"})
; (get-in full-name [:name])

; (-> (hash-map :name "Felipe" :last-name "Assoline")
;     (get-in [:name]))