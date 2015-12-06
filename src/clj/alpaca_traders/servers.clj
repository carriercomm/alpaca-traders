(ns alpaca-traders.servers
  (:require 
    [alpaca-traders.rethink-driver :as driver]
    [rethinkdb.query :as r]))

;TODO: Server side cache would be good. This table doesn't really change

(defn get-servers [] 
  (with-open [conn (driver/get-conn)] 
    (-> (r/db driver/DB_NAME)
        (r/table "servers")
        (r/run conn)
        )))
