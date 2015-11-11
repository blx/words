(ns words.lib
  (:require [clojure.string :as str]))

(def words
  (-> (slurp "resources/private/words")
      str/split-lines))

(def words-count
  (count words))

(def random-word
  (partial rand-nth words))

(defn md5-base10 [^String s]
  (let [md5 (java.security.MessageDigest/getInstance "md5")]
    (->> (.getBytes s)
         (.digest md5)
         (BigInteger. 1))))

(defn hash-word
  [word]
  (-> (md5-base10 word)
      (mod Long/MAX_VALUE)
      long))

(defn get-rng [seed-word]
  (java.util.Random. (hash-word seed-word)))

(defn random-words [seed-word]
  (let [rng (get-rng seed-word)
        next-index #(.nextInt rng words-count)]
    (->> (repeatedly next-index)
         (map (partial nth words)))))

(defn page-words [seed-word]
  (->> seed-word
       random-words
       (take 50)))

(def page-cache
  (zipmap words (map page-words words)))
