(ns http-proxy.core
  (:require [org.httpkit.server :as server])
  (:gen-class))

(defn app [req]
  (println req)
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "hello HTTP!"})

(defn -main
  [& args]
  (println "Starting server on port 8080")
  (server/run-server app {:port 8080}))
