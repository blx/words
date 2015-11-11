(ns words.lib
  (:require [clojure.string :as str]))

(defn upper-case? [s]
  (= (str s) (str/upper-case s)))

(def words
  (->> (slurp "resources/private/words")
       str/split-lines
       (remove (comp upper-case? first))))

(def words-count
  (count words))

(def random-word
  (partial rand-nth words))

(defn md5-base10 [s]
  (let [md5 (java.security.MessageDigest/getInstance "md5")]
    (->> (.getBytes s)
         (.digest md5)
         (BigInteger. 1))))

(defn hash-word
  "Return the index onto `words` obtained by hashing `word`."
  [word]
  (let [hash (str (md5-base10 word))
        len (min (count hash)
                 (-> (str words-count) count inc))]
    (-> hash
        (subs 0 len)
        Long/parseLong)))

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

