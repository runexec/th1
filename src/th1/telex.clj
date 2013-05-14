(ns th1.telex
  (import [clojure.lang PersistentHashMap]))

(defprotocol Telex
   (signals [_] "returns all signals")
   (commands [_] "returns all commands")
   (headers [_] "returns all headers")
   (to [_] "ip:port of recipient")
   (br [_] "bytes received from recipient")
   (ring [_] "ring number")
   (line [_] "product of two ring values")
   (hop [_] "relay hop count"))

(defrecord TData [^PersistentHashMap signals
                  ^PersistentHashMap commands
                  ^PersistentHashMap headers])
(defn tdata
   [signals-hmap
    commands-hmap
    headers-hmap]
   (let [signals (when signals-hmap
                   (zipmap
                    (map #(str "+" (name %))
                         (keys signals-hmap))
                    (vals signals-hmap)))
         commands (when commands-hmap
                    (zipmap
                     (map #(str "." (name %))
                          (keys commands-hmap))
                     (vals commands-hmap)))
         headers (when headers-hmap
                   (zipmap
                    (map #(str "_" (name %))
                         (keys headers-hmap))
                    (vals headers-hmap)))]
    (TData. signals commands headers)))

(defrecord Packet [_to _br _ring _line _hop tdata])

(extend-type Packet
  Telex
  (signals [_] (-> _ :tdata :signals))
  (commands [_] (-> _ :tdata :commands))
  (headers [_] (-> _ :tdata :headers))
  (to [_] (:_to _))
  (br [_] (:_br _))
  (ring [_] (:_ring _))
  (line [_] (:_line _))
  (hop [_] (:_hop _)))

(defn packet [{:keys [to br ring line hop -tdata]}]
  (Packet. to br ring line hop (or -tdata
                                   (tdata nil nil nil))))


