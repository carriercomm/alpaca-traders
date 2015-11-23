(ns alpaca-traders.items
  (:require 
    [alpaca-traders.rethink-driver :as driver]
    [rethinkdb.query :as r]
    [secretary.core :as secretary :refer-macros [defroute]]))

(def test-items [{:name "Choose an item" :id nil}
                 {:name "Bone chips" :id 1}
                 {:name "Duck chips" :id 2}])

(defn get-items [] 
  (with-open [conn (driver/get-conn)] 
    (-> (r/db driver/DB_NAME)
        (r/table "items")
        (r/run conn)
        ))) 
