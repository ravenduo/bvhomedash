(ns brainiac.plugins.twitter-search
  (:use [clojure.contrib.json :only (read-json)]
        [clojure.java.io :only (reader)])
  (:require [brainiac.plugin :as brainiac]))

(defn format-tweet [tweet]
  {
   :name (:from_user_name tweet)
   :handle (:from_user tweet)
   :text (:text tweet)
   :profile_image_url (:profile_image_url_https tweet)
   })

(defn transform [stream]
  (let [json (read-json (reader stream))]
  (assoc {}
    :name "twitter-search"
    :data (map format-tweet (:results json)))))

(defn url [term]
  (format "http://search.twitter.com/search.json?q=%s" term))

(defn configure [{:keys [term program-name]}]
  (brainiac/simple-http-plugin
    {:url (url term)}
    transform program-name))
