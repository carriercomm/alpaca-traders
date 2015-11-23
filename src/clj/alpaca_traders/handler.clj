(ns alpaca-traders.handler
  (:require [alpaca-traders.items :as items]
            [cheshire.core :refer [generate-string]]
            [compojure.core :refer [GET POST defroutes]]
            [compojure.route :refer [not-found resources]]
            [hiccup.core :refer [html]]
            [hiccup.page :refer [include-js include-css]]
            [prone.middleware :refer [wrap-exceptions]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [environ.core :refer [env]]
            ))

(def home-page
  (html
    [:html
     [:head
      [:meta {:charset "utf-8"}]
      [:meta {:name "viewport"
              :content "width=device-width, initial-scale=1"}]
      (include-css (if (env :dev) "css/site.css" "css/site.min.css"))]
     [:body
      [:div#app
       [:h3 "ClojureScript has not been compiled!"]
       [:p "please run "
        [:b "lein figwheel"]
        " in order to start the compiler"]]
      (include-js "js/app.js")]]))

(defn save-document [doc]
  (print doc)
  {:status "ok"})

(defn json-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json"}
   :body (generate-string data)})

(defroutes routes
  (GET "/" [] home-page)
  (GET "/items" [] (json-response (items/get-items)))
  (GET "/new-posting" [] "hi")
  (POST "/new-posting" {:keys [body-params]}
        (save-document body-params))
  (resources "/")
  (not-found "Not Found"))

(def app
  (let [site-prefs (assoc-in site-defaults [:security :anti-forgery] false)
        handler (wrap-defaults #'routes site-prefs)]
    (print site-prefs)
    (if (env :dev) 
      (-> handler wrap-exceptions wrap-reload) 
      handler)))
