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
      (println "\tNo need to create DB. Exists")
      (r/run (r/db-create db) conn))
    ))

(defn table-exists? [conn db table-name] 
  (some #{table-name} (r/run (r/table-list) conn)))

(defn drop-table [conn db table-name] 
  "create table if eists"
  (println "Dropping table: " db ":" table-name)
  (if (table-exists? conn db table-name)
    (-> (r/db db)
        (r/table-drop table-name)
        (r/run conn))
    (println "\tNo need to drop table. Does not exist.")
    ))

(defn create-table [conn db table-name] 
  "create table if eists"
  (println "Creating table: " db ":" table-name)
  (if (table-exists? conn db table-name)
    (println "\tNo need to create table. Exists.")
    (-> (r/db db)
        (r/table-create table-name)
        (r/run conn))
    ))

(defn index-exists? [conn db table-name index-name] 
  (some #{index-name} (-> (r/db db)
                          (r/table table-name)
                          (r/index-list)
                          (r/run conn))))

(defn create-index-server-item [conn db]
  "Creates a compound index on server and item in the table 'listings'"
  (let [table-name "listings"
        index-name "server-item"]
    (println "Creating listings index: " db ":" index-name)
    (if-not (index-exists? conn db table-name index-name)
      (-> (r/db db)
          (r/table table-name)
          (r/index-create index-name 
                          (r/fn [row]
                                [(r/get-field row :server-id)
                                 (r/get-field row :item-id)]))
          (r/run conn))
      (println "\tDidn't create index")
      )))

(defn create-simple-index [conn db table-name index-kw] 
  (if-not (index-exists? conn db table-name index-kw)
      (-> (r/db db)
          (r/table table-name)
          (r/index-create index-kw)
          (r/run conn))
      ;TODO: wait for index creation?
      ))

(defn load-documents [conn db table-name] 
  (println "Loading documents for table " table-name)
  (-> (r/table table-name)
      (r/insert (-> (str "json/" table-name ".json")
                    (slurp)
                    (parse-string true)))
      (r/run conn)))

(defn setup-db []
  "Create the DB and all tables for our app."
  (with-open [conn (get-conn)]
    
    (create-db (get-conn) DB_NAME)
    
    (let [drop-table #(drop-table conn DB_NAME %)]
      (drop-table "items")
      (drop-table "servers"))
    
    ;Yes.. I tried a map. It was just chugging.. Parallel map by default 
    ;perhaps causing lock up on db connection?
    (let [make-table #(create-table conn DB_NAME %)]
      (make-table "users")
      (make-table "listings")
      (make-table "items")
      (make-table "servers"))
    
    (create-index-server-item conn DB_NAME)
    (let [make-index #(create-simple-index conn DB_NAME "listings" %)]
      (make-index :server)
      (make-index :item))
    
    ;Init the DB with the items listed in the json file. 
    (let [load-docs #(load-documents conn DB_NAME %)]
      (load-docs "items")
      (load-docs "servers"))
    ))

;(setup-db)
