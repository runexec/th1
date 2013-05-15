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
  "Data to add to Telex. All args are hmaps."
   [{:keys [signals
            commands
            headers]}]
   (let [signals (when signals
                   (zipmap
                    (map #(str "+" (name %))
                         (keys signals))
                    (vals signals)))
         commands (when commands
                    (zipmap
                     (map #(str "." (name %))
                          (keys commands))
                     (vals commands)))
         headers (when headers
                   (zipmap
                    (map #(str "_" (name %))
                         (keys headers))
                    (vals headers)))]
    (TData. signals commands headers)))

(defrecord Packet [_to _br _ring _line _hop])

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

(defn packet [{:keys [to br ring line hop -tdata remove-nil?]}]
  (let [{:keys
         [signals 
          commands 
          headers] :as att} -tdata
          attributes (apply merge (vals att))
          telex (loop [p (Packet. to br ring line hop)
                       a attributes]
                  (if-not (seq a) p
                          (let [_ (first a)
                                [k v] [(key _) (val _)]]
                            (recur
                             (assoc p (keyword k) v)
                             (rest a)))))]
    (if-not remove-nil? telex
            (loop [t telex]
              (if-not (->> t
                           vals
                           (some nil?))
                t
                (let [_ (first t)
                      [k v] [(key _) (val _)]]
                  (recur
                   (if (nil? v)
                     (dissoc t k)))))))))
           


