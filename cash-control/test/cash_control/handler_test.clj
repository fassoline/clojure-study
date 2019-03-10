(ns cash-control.handler-test
  (:require [midje.sweet :refer :all]
            [ring.mock.request :as mock]
            [cheshire.core :as json]
            [cash-control.handler :refer :all]
            [cash-control.db :as db]))

(facts "Hello world, root route"
       (fact "status: 200"
             (let [response (app (mock/request :get "/"))]
               (:status response) => 200))
       (fact "body is 'Hello World'")
       (let [response (app (mock/request :get "/"))]
         (:body response) => "Hello World"))

(facts "Invalid route"
       (fact "status: 404"
             (let [response (app (mock/request :get "/invalid"))]
               (:status response) => 404)))

(facts "Initial balance is 0"
       (against-background (json/generate-string {:balance 0}) => "{\"balance\":0}")

       (let [response (app (mock/request :get "/balance"))]
         (fact "content-type is application/json;charset=utf-8"
               (get-in response [:headers "Content-Type"]) => "application/json; charset=utf-8")

         (fact "status: 200"
               (:status response) => 200)

         (fact "body is '0'"
               (:body response) => "{\"balance\":0}")))

(facts "Registra uma receita no valor de 10"
       (against-background (db/save {:value 10 :type "income"}) => {:id 1 :value 10 :type "income"})
       (let [response (app (-> (mock/request :post "/transactions")
                               (mock/json-body {:value 10 :type "income"})))]
         
         (fact "o status da resposta é 201"
               (:status response) => 201)

         (fact "o texto do corpo é um JSON com o conteúdo enviado e um id"
               (:body response) => "{\"id\":1,\"value\":10,\"type\":\"income\"}")))