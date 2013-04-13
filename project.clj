(defproject ssh-circle "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clj-http "0.5.6"]
                 [cheshire "5.1.1"]
                 [clojure-complete "0.2.2"]
                 [circle/wait-for "1.0.0"]
                 [clj-time "0.4.4"]
                 [org.clojure/tools.logging "0.2.3"]]
  :jar-name "ssh-circle.jar"
  :uberjar-name "ssh-circle-standalone.jar"
  :main ssh-circle.core)
