(ns lambda.request
  (:require [clojure.data.json :as json])
  (:gen-class))

(defn lambda-request->request [lambda-request]
  (let [{http-method "httpMethod", body-map "body", {path "proxy"} "pathParameters"} lambda-request
        body (json/write-str body-map)]
    {:path path :http-method http-method :body body}))

(defn path [request]
  (get request :path))

(defn http-method [request]
  (get request :http-method))

(defn body [request]
  (get request :body))