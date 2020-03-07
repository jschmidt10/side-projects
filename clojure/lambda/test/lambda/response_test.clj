(ns lambda.response-test
  (:require [clojure.data.json :as json]
            [clojure.test :refer :all]
            [lambda.response :as res]))

(def status-code 200)
(def headers {"Content-Type" "application/json"})
(def body {"type" "json"})

(def raw-response
  {:status-code status-code :headers headers :body body})

(deftest should-create-response
  (let [lambda-response (res/response->lambda-response raw-response)
        {actual-status-code "statusCode" actual-headers "headers" actual-body "body"} lambda-response]
    (is (= status-code actual-status-code))
    (is (= headers actual-headers))
    (is (= body actual-body))))