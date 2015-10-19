(ns alpaca-traders.handler
  (:require [compojure.core :refer [GET POST defroutes]]
            [compojure.route :refer [not-found resources]]
            [hiccup.core :refer [html]]
            [hiccup.page :refer [include-js include-css]]
            [prone.middleware :refer [wrap-exceptions]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [environ.core :refer [env]]
            )
  )

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

(defroutes routes
  (GET "/" [] home-page)
  (GET "/new-posting" [] "hi")
  (POST "/new-posting" {:keys [body-params]}
        (save-document body-params))
  (resources "/")
  (not-found "Not Found"))

(def app
  (let [handler (wrap-defaults #'routes site-defaults)]
    (if (env :dev) (-> handler wrap-exceptions wrap-reload) handler)))
