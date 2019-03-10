(ns cash-control.test-helpers
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [cash-control.handler :refer [app]]
            [clj-http.client :as http]))

(def web-server (atom nil))
(def web-server-port 3001)
(def base-url (str "http://localhost:" web-server-port))

(defn url-for [path] (str base-url path))
(def request-for (comp http/get url-for))
(defn body-from [path] (:body (request-for path)))

(defn start-web-server
  [port]
  (swap! web-server
         (fn [_] (run-jetty app {:port port :join? false}))))

(defn stop-web-server []
  (.stop @web-server))