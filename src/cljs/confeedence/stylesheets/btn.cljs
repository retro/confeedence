(ns confeedence.stylesheets.btn)

(defn stylesheet []
  [:.btn {:cursor 'pointer
          :-webkit-appearance 'none}
   [:&:focus {:outline 'none}]
   [:&:active {:outline 'none}]])
