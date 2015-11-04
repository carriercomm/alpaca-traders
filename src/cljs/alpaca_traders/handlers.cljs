(ns alpaca-traders.handlers
  (:require 
    [alpaca-traders.db :as db]
    [re-frame.core :refer [register-handler path trim-v]]
    ))

;; Register the handlerz

(register-handler 
  ;; TODO: Possibly pull in some user settings from session
  :initialize-db
  (fn [_ _] db/initial-state) 
  )

(register-handler 
  :select-item [(path :item) trim-v]
  (fn [old-item [item]] item) 
  )

(register-handler 
  :select-server [(path :server) trim-v]
  (fn [old-server [server]] server) 
  )

(register-handler
  :search-for-listings
  [(path :listings) trim-v]
  (fn [listings [item server]]
    (db/get-listings item server)
    )
  )
  