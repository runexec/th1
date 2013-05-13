(ns th1.dht
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
