(ns lambda.response
  (:require [clojure.data.json :as json])
  (:gen-class))

(defn response->lambda-response [response]
  (let [{status-code :status-code, headers :headers, body :body} response]
    {"statusCode" status-code "headers" headers "isBase64Encoded" false "body" body}))