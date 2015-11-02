(ns alpaca-traders.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]
            [alpaca-traders.db :refer [initial-state]]
    )
  )

;;Register some subs

(register-sub 
  :items
  (fn [db _]
    (print db)
    (reaction (:items @db))
    )
  )

(register-sub 
  :item
  (fn [db _]
    (reaction (:item @db))
    )
  )

(register-sub 
  :server
  (fn [db _]
    (reaction (:server @db))
    )
  )

(register-sub 
  :servers 
  (fn [db _]
    (reaction (:servers @db))
    )
  )
