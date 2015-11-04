(ns alpaca-traders.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [re-frame.core :as re-frame :refer [dispatch]]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [alpaca-traders.about :as about :refer [view]]
            [alpaca-traders.handlers]
            [alpaca-traders.nav-bar :as nav-bar :refer [create]]
            [alpaca-traders.new-posting :as post :refer [create]]
            [alpaca-traders.postings :as postings :refer [view]]
            [alpaca-traders.search :as search :refer [create]]
            [alpaca-traders.subs])
  (:import goog.History)
  )

(enable-console-print!)

;; -------------------------
;; Views
(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" [selected-item] 
                    (session/put! :current-page #'search/create))

(secretary/defroute 
    "/search/:item/:server" 
    [item server]
    (re-frame/dispatch [:select-item item])
    (re-frame/dispatch [:select-server server])
    (session/put! :current-page #'search/create)
  )

(secretary/defroute "/about" []
                    (session/put! :current-page #'about/view))

(secretary/defroute "/post/create" []
                    (session/put! :current-page #'post/create))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
      EventType/NAVIGATE
      (fn [event]
        (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

(def app-root
  [:div 
   [nav-bar/create] 
   [current-page]
   ])

;; -------------------------
;; Initialize app
(defn mount-root []
  (reagent/render app-root (.getElementById js/document "app"))
  )

(defn init! []
  (re-frame/dispatch [:initialize-db])
  (hook-browser-navigation!)
  (mount-root))
