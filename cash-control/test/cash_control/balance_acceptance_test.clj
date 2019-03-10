(ns cash-control.balance-acceptance-test
  (:require [midje.sweet :refer :all]
            [cash-control.test-helpers :refer :all]
            [cheshire.core :as json]))

(against-background
 [(before :facts (start-web-server web-server-port))
  (after :facts (stop-web-server))]

 (fact "initial balance is 0" :acceptance-test
       (json/parse-string (body-from "/balance") true) => {:balance 0}))