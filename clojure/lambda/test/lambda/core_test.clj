(ns lambda.core-test
  (:require [clojure.data.json :as json]
            [clojure.test :refer :all]
            [lambda.core :as core]
            [lambda.request :as req]))

(def lambda-request {"httpMethod" "GET"
              "body" "{\"body\": true}"
              "pathParameters" {"proxy" "/somepath"}})

(def expected-lambda-response
  {"statusCode" 200 "headers" {} "isBase64Encoded" false "body" (get lambda-request "body")})

(deftest should-process-echo
  (let [request-str (json/write-str lambda-request)
        lambda-response (core/-handle_request request-str)]
    (is (= (json/write-str expected-lambda-response) lambda-response))))