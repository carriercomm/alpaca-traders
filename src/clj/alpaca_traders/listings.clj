(ns alpaca-traders.listings
  (:require 
    [alpaca-traders.rethink-driver :as driver]
    [rethinkdb.query :as r]))

;TODO: move to map probably.
(defonce ALL "ALL")
(defonce SERVER "server")
(defonce ITEM "item")
(defonce SERVER-ITEM "server-item")

; (defn wildcard? [value] (= value "*")
;   )

; (defn query-type [item server] 
;   (if (every? wildcard? [item server])
;     "ALL-LISTINGS"
;     )
;   )

(defn query [query-params] 
  (let [{:keys [item server]} query-params]
    (print query-params)(print query-params)(print query-params)(print query-params)
     (sorted-map))
  
  ; (if (every? wildcard? [item server])
    
  ;   )
    
  ; (with-open [conn (driver/get-conn)] 
  ;   (-> (r/db driver/DB_NAME)
  ;       (r/table "listings")
  ;       (r/run conn)))
  
  )