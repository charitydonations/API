(defproject api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure  "1.3.3"]
                 [com.novemberain/monger  "2.0.1"]
                 [crypto-password  "0.1.3"]
                 [crypto-random  "1.2.0"]
                 [io.forward/sendgrid-clj  "1.0"]
                 [abengoa/clj-stripe  "1.0.4"]]
  :ring {:handler api.routes/app})
