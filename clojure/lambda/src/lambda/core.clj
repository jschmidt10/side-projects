(ns lambda.core
  (:require [clojure.data.json :as json]
            [lambda.request :as req]
            [lambda.response :as res])
  (:gen-class
    :methods [^:static [handle_request [String] String]]))

(defn process-echo [request]
  {:status-code 200 :headers {} :body (json/read-str (get request :body))})

(defn process-lambda-echo [lambda-request]
  (let [request (req/lambda-request->request lambda-request)
        response (process-echo request)]
    (res/response->lambda-response response)))

(defn -handle_request [request-str]
  (let [lambda-request (json/read-str request-str)
        lambda-response (process-lambda-echo lambda-request)]
    (json/write-str lambda-response)))

(defn -main
  [& args]
  (println "Hello, Lambda!"))