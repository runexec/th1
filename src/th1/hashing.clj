(ns th1.hashing
  (import java.security.MessageDigest))

(defn switch-id [ip port]
  (->> (.digest
        (doto (MessageDigest/getInstance "SHA-1")
          (.update
           (.getBytes (str ip ":" port)))))
       (map #(format "%x" %))
       (apply str)))
      
