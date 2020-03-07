(ns lambda.request-test
  (:require [clojure.data.json :as json]
            [clojure.test :refer :all]
            [lambda.request :as req]))

(def lambda-request
  {"httpMethod" "GET"
   "body" "{\"body\": true}"
   "pathParameters" {"proxy" "/somepath"}})

(deftest should-create-request 
  (let [request (req/lambda-request->request lambda-request)
        expected-http-method (get lambda-request "httpMethod")
        expected-path (get (get lambda-request "pathParameters") "proxy")
        expected-body (json/write-str (get lambda-request "body"))]
    (is (= expected-http-method (req/http-method request)))
    (is (= expected-path (req/path request)))
    (is (= expected-body (req/body request)))))