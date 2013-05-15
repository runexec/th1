# th1

Work in progress

```clojure
th1.telex> 
(def telex-data
  (tdata
   {:commands (hash-map "CommandExample" "abc")
    :signals (hash-map "SignalExample" "def")
    :headers (hash-map "HeaderExample" "ghi")}))
#'th1.telex/telex-data
th1.telex> 
(clojure.pprint/pprint
 (packet {:-tdata telex-data}))
{:_to nil,
 :_br nil,
 :_ring nil,
 :_line nil,
 :_hop nil,
 :+SignalExample "def",
 :.CommandExample "abc",
 :_HeaderExample "ghi"}
nil
th1.telex> 
(clojure.pprint/pprint
 (packet {:remove-nil? true 
	  :-tdata telex-data}))
{:+SignalExample "def", :.CommandExample "abc", :_HeaderExample "ghi"}
nil

```
