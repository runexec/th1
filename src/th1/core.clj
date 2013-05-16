(ns th1.core
  (:use cheshire.core))

(defn json [coll & [pretty?]]
  (let [form (partial generate-string
                      coll)]
    (if-not pretty?
      (form)
      (form {:pretty true}))))
