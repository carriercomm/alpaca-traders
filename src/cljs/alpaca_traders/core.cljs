(ns alpaca-traders.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            
            [alpaca-traders.about :as about :refer [view]]
            [alpaca-traders.new-posting :as post :refer [create]]
            [alpaca-traders.postings :as postings :refer [view]]
            [alpaca-traders.nav-bar :as nav-bar :refer [create]])
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

(secretary/defroute "/" []
                    (session/put! :current-page #'postings/view))

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

;; -------------------------
;; Initialize app
(defn mount-root []
  (reagent/render [:div 
                   [nav-bar/create] 
                   [current-page]
                   ]
                  (.getElementById js/document "app")))

(defn init! []
  (hook-browser-navigation!)
  (mount-root))
