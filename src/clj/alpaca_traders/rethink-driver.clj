(ns alpaca-traders.rethink-driver
  (:require [rethinkdb.query :as r]))

;Create tables and db if not already created?

(with-open 
  [conn (r/connect :host "127.0.0.1" :port 28015 :db "alpaca")]
  
  ; (r/run (r/db-create "alpaca") conn)
  
  ; (map 
  ;   #(-> (r/table-create %) (r/run conn)) ["items" "users" "listings"])
  
  ; (-> (r/db "alpaca") (r/table-create "items") (r/run conn))
  
  (-> (r/table "items")
      (r/insert [
                 {:name "Bone chips"
                  :external_id 123},
                 {:name "Duck chips"
                  :external_id 1234}
                 ])
      (r/run conn)
  	)
  
  )