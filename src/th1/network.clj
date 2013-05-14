(ns th1.network
  (:import java.net.NetworkInterface))

(defn nics []
  (->> (NetworkInterface/getNetworkInterfaces)
       enumeration-seq 
       (map #(assoc {}
               :display-name (.getDisplayName %)
               :name (.getName %)
               :addresses (->> %
                               .getInetAddresses
                               enumeration-seq)))))

                  
               

