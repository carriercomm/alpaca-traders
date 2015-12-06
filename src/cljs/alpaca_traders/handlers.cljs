(ns alpaca-traders.handlers
  (:require 
    [alpaca-traders.db :as db]
    [re-frame.core :refer [dispatch path register-handler trim-v]]
    [ajax.core :as ajax]))

(def json-response-format 
  "Define the response format for our GET calls. The only difference is that we want to keywordize every single one of the responses."
  (ajax/json-response-format {:keywords? true}))

;; Register the handlerz

(register-handler 
  :initialize-db
  (fn [_ _] 
    (dispatch [:fetch-items])
    (dispatch [:fetch-servers])
    db/initial-state))

(register-handler
  :got-items [(path :items) trim-v]
  (fn [old-items [items]]
    (concat db/initial-items items)))

(register-handler
  :fetch-items
  (fn [db _] 
    (ajax/GET "/items" 
              {:handler #(dispatch [:got-items %])
               :response-format json-response-format})
    db))

(register-handler
  :got-servers [(path :servers) trim-v]
  (fn [old-servers [servers]]
    (concat db/initial-servers servers)))

(register-handler 
  :fetch-servers
  (fn [db _]
    (ajax/GET "/servers"
          {:handler #(dispatch [:got-servers %])
           :response-format json-response-format})
    db))

(register-handler
  :got-listings [(path :listings) trim-v]
  (fn [old-listings [listings]] listings))

(register-handler
  :search-for-listings
  (fn [db _]
    (let [{:keys [selected-item selected-server]} db] 
      (ajax/GET "/listings"
                {:handler #(dispatch [:got-listings %])
                 :params 
                   { :item (if selected-item selected-item "*")
                     :server (if selected-server selected-server "*")}
                 :response-format json-response-format}))
    db)) ; No change to state, return old piece of state

(register-handler 
  :select-item [(path :selected-item) trim-v]
  (fn [old-item [item]] item))

(register-handler 
  :select-server [(path :selected-server) trim-v]
  (fn [old-server [server]] server))
