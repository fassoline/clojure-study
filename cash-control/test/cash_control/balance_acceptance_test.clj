(ns cash-control.balance-acceptance-test
  (:require [midje.sweet :refer :all]
            [cash-control.test-helpers :refer :all]
            [cheshire.core :as json]
            [clj-http.client :as http]))

(against-background
 [(before :facts (start-web-server web-server-port))
  (after :facts (stop-web-server))]

 (fact "initial balance is 0" :acceptance-test
       (json/parse-string (body-from "/balance") true) => {:balance 0})
 (fact "O saldo é 10 quando a única transação é uma receita de 10" :acceptance-test
       (http/post (url-for "/transactions")
                  {:content-type :json
                   :body (json/generate-string {:value 10 :type "income"})})
       (json/parse-string (body-from "/balance") true) => {:balance 10}))