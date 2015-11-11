(ns words.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :refer [redirect]]
            [hiccup.core :refer [html]]
            [words.lib :as lib]))

(defn page [title content]
  (let [css-rules ["body {width:600px; margin:2em auto; line-height:1.7em;}"
                   "h1 {text-align:center;}"]]
    (html [:html
           [:head
            [:title title]
            [:style (reduce str css-rules)]]
           [:body content]])))

(defn word-page [seed-word words]
  (html [:h1 seed-word]
        [:p (interpose " " (map #(vector :a {:href %} %) words))]))

(defroutes app-routes
  (GET "/" [] (redirect (str "/word/" (lib/random-word))))
  (GET "/word/:word" [word] (->> word lib/page-words (word-page word) (page word)))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
