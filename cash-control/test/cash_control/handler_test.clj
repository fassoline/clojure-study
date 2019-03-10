(ns cash-control.handler-test
  (:require [midje.sweet :refer :all]
            [ring.mock.request :as mock]
            [cheshire.core :as json]
            [cash-control.handler :refer :all]))

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