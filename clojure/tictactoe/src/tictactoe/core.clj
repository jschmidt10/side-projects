(ns tictactoe.core
  (:gen-class))

(def three-in-a-rows
  (list
   '(0 1 2)
   '(3 4 5)
   '(6 7 8)
   '(0 3 6)
   '(1 4 7)
   '(2 5 8)
   '(0 4 8)
   '(2 4 7)))

(defn is-blank [s] (= " " s))
(defn is-not-blank [s] (not (is-blank s)))

(defn fetch-row [board row]
  (map #(nth board %) row))

(defn all-same
  "Checks if the same player holds all 3 slots"
  [board row]
  (let [dc (distinct (fetch-row board row))]
    (if (and
         (= 1 (count dc))
         (is-not-blank (first dc)))
      dc
      nil)))

(defn is-full [board]
  (empty? (filter is-blank board)))

(defn is-game-over [board]
  (if
    ))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
