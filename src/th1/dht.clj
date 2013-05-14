(ns th1.dht
  (:require [th1.telex :as telex])
  (import java.security.MessageDigest))

(defn switch-id [ip port]
  (->> (.digest
        (doto (MessageDigest/getInstance "SHA-1")
          (.update
           (.getBytes (str ip ":" port)))))
       (map #(format "%x" %))
       (apply str)))

(defn switch-distance [switch-id switch-id2]
    (apply str
           (map #(bit-xor (int %1)
                          (int %2))
                switch-id
                switch-id2)))

(let [default "a9993e364706816aba3e25717850c26c9cd0d89d"]
  (defn seed 
    ([] (seed nil default))
    ([& [ip-port end-sha1]]
       (let [[headers commands] (repeat nil)
             signals (hash-map "end" 
                               (or end-sha1 default))]
         (telex/packet 
          {:to (or ip-port "telehash.org:42424")
           :-tdata (telex/tdata
                    signals
                    commands
                    headers)})))))
