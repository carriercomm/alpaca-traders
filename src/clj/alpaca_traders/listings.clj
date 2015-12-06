(ns alpaca-traders.listings
  (:require 
    [alpaca-traders.rethink-driver :as driver]
    [rethinkdb.query :as r]))

(defn query [query-params] 
  (let [{:keys [item server]} query-params]
    (print query-params)(print query-params)(print query-params)(print query-params)
     (sorted-map))
     
  (with-open [conn (driver/get-conn)] 
    (-> (r/db driver/DB_NAME)
        (r/table "listings")
        (r/run conn))))