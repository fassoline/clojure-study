(ns cash-control.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [cheshire.core :as json]
            [cash-control.db :as db]
            [ring.middleware.json :refer [wrap-json-body]]))

(defn response-as-json [body & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json; charset=utf-8"}
   :body (json/generate-string body)})

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/balance" [] (response-as-json {:balance 0}))
  (POST "/transactions" request (-> (db/save (:body request))
                                    (response-as-json 201)))
  (route/not-found "Not Found"))

(def app
  (-> (wrap-defaults app-routes api-defaults)
      (wrap-json-body {:keywords? true :bigdecimals? true})))