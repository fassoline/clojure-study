(ns conversor.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clj-http.client :as http-client]
            [cheshire.core :as json])
  (:gen-class))

(def opcoes-do-programa
  [["-d" "--de moeda base" "moeda base para conversão"]
   ["-p", "--para moeda destino" "moeda qual queremos saber o valor"]])

(def chave "")
(def api-url "https://free.currencyconverterapi.com/api/v6/convert")

(defn parametrizar-moedas
  [de para]
  (str de "_" para))

(defn create-api-query-string
  [de para]
  (hash-map :query-params {"q" (parametrizar-moedas de para) "apiKey" chave}))

(defn obter-cotacao
  [de para]
  (-> (http-client/get api-url (create-api-query-string de para))
      (:body)
      (json/parse-string)
      (get-in ["results", "USD_BRL", "val"])))

(defn formatar
  [cotacao de para]
  (str "1 " de " equivale a " cotacao " em " para))

(defn -main
  [& args]
  (let [{:keys [de para]} (:options (parse-opts args opcoes-do-programa))]
    (-> (obter-cotacao de para)
        (formatar de para)
        (prn))))

(-main "--de=USD" "--para=BRL")

; (def full-name {:name "Felipe" :last-name "Assoline"})
; (get-in full-name [:name])

; (-> (hash-map :name "Felipe" :last-name "Assoline")
;     (get-in [:name]))

