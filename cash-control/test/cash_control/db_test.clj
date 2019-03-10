(ns cash-control.db-test
  (:require [midje.sweet :refer :all]
            [cash-control.db :refer :all]))

(facts "Guarda uma transação num átomo"       
       (against-background [(before :facts (clear-db))]
       
       (fact "a transação é o primeiro registro"
             (save {:value 7 :type "income"})
             => {:id 1 :value 7 :type "income"})))