(ns alpaca-traders.rethink-driver
  "See /misc-docs/rethink-schema.pdf for more deetz on the schema."
  (:require [rethinkdb.query :as r])
  (:require [cheshire.core :refer [parse-string]]))

(defonce DB_ADDR "127.0.0.1")
(defonce DB_NAME "alpaca_trade_test1")
(defonce DB_PORT 28015)

(defn get-conn []
  (r/connect :host DB_ADDR :port DB_PORT :db DB_NAME))

(defn create-db [conn db] 
 "create db if exists."
  (println "Creating DB: " db)
  (let [db-exists? (some #{db} (r/run (r/db-list) conn))]
    (if db-exists?
      (println "No need to create DB. Exists")
      (r/run (r/db-create db) conn))
    ))

(defn create-table [conn db table-name] 
 "create table if eists"
  (println "Creating table: " db ":" table-name)
  (let [table-exists? (some #{table-name} (r/run (r/table-list) conn))]
    (if table-exists?
      (println "No need to create table. Exists.")    
      (-> (r/db db)
          (r/table-create table-name)
          (r/run conn))
      )
    ))

(defn setup-db []
  "Create the DB and all tables for our app."
  (with-open [conn (get-conn)]
    (create-db (get-conn) DB_NAME)
    
    ;Yes.. I tried a map. It was just chugging.. Parallel map by default 
    ;perhaps causing lock up on db connection?
    (let [make-table #(create-table conn DB_NAME %)]
      (make-table "items")
      (make-table "users")
      (make-table "listings")
      )
    
    ;Init the DB with the items listed in the json file. 
    (-> (r/table "items")
        (r/insert (-> (slurp "items.json")
                      (parse-string true)))
        (r/run conn))
    ))

;(setup-db)
  