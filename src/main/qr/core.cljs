(ns qr.core
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]
            ["@material-ui/core" :as mui]))

(def host (r/atom ""))
(def aid (r/atom ""))
(def cid (r/atom ""))

(defn join-as-url
  [host aid cid]
  (str "https://"
       (if (zero? (count @host)) "18.220.196.14:3006" @host)
       "/chat"
       (cond
         (and (zero? (count @aid)) (zero? (count @cid))) ""
         (and (pos? (count @aid)) (zero? (count @cid))) (str "?aid=" @aid)
         (and (zero? (count @aid)) (pos? (count @cid))) (str "?cid=" @cid)
         :default (str "?aid=" @aid "&cid=" @cid)
         )))

(defn change [v] #(reset! v (-> % .-target .-value)))

(defn main []
  [:div
   [:> mui/Typography {:variant "h2"} "QR CODE GENERATOR"]
   [:> mui/Grid {:container true}
    [:> mui/Grid {:item true :xs 4}
     [:div
      [:> mui/TextField {:id "host"
                         :label "Host"
                         :value @host
                         :on-change (change host)}]]
     [:div
      [:> mui/TextField {:id "agency"
                         :label "Agency"
                         :value @aid
                         :on-change (change aid)}]]
     [:div
      [:> mui/TextField {:id "customer"
                         :label "Customer"
                         :value @cid
                         :on-change (change cid)}]]]
    [:> mui/Grid {:item true :xs 8}
     [:img {:src (str "https://api.qrserver.com/v1/create-qr-code/?data="
                      (js/encodeURIComponent (join-as-url host aid cid)))}]
     [:h3 (join-as-url host aid cid)]]]
   ])

(defn init []
  (rdom/render [main] (js/document.getElementById "app")))

(init)

