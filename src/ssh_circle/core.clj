(ns ssh-circle.core
  (:require [clj-http.client :as http]
            [cheshire.core :as json]
            [clj-time.core :as time]
            [circle.wait-for :refer (wait-for)]
            [clojure.java.shell :refer (sh)]
            [clojure.tools.logging :refer (infof errorf)])
  (:gen-class))

(def token (System/getenv "CIRCLE_TOKEN"))

(defn api-call [method path & [opts]]
  (let [resp (http/request
              (merge {:method method
                      :url (str "https://circleci.com/api/v1/" (name path))
                      :headers {"circle-token" token}
                      :accept :json}
                     opts))]
    (-> resp :body (json/parse-string true))))

(defn builds [project]
  (api-call :get (format "project/%s" project)))

(defn single-build [project build_num]
  (api-call :get (format "project/%s/%s" project build_num)))

(defn create-ssh-build [project build_num]
  (api-call :post (format "project/%s/%s/ssh" project build_num)))

(defn build-num-to-retry [project]
  (->> (builds project)
       (filter #(= "master" (-> % :branch)))
       first
       :build_num))

(defn find-ssh-info [build-json]
  (some->> build-json
           :steps
           (filter #(-> % :name (= "Enable SSH")))
           first
           :actions
           first
           :out
           first
           :message
           (re-find #"\$ ssh -p (\d+) (\w+)\@([\d\.]+)")
           (zipmap [:out :port :username :ip])))

(defn can-ssh-build? [build-json]
  (and (-> build-json :why (= "ssh"))
       (->> build-json :lifecycle (contains? #{"running" "queued"}))))

(defn ssh-instructions [project build_num]
  (let [build (single-build project build_num)]
    (if-not (can-ssh-build? build)
      {:error "can't ssh into build"}
      (find-ssh-info build))))

(defn assert-and-return
  ([x]
     (assert x)
     x)
  ([x message]
     (assert x message)
     x))

(defn get-ssh-instructions [project]
  (let [build_num (->> (build-num-to-retry project)
                       (create-ssh-build project)
                       :build_num
                       (assert-and-return))
        _ (infof "Created ssh build: https://circleci.com/gh/%s/%s"
                 project build_num)
        instructions (wait-for {:success-fn identity
                                :sleep (time/millis 5000)
                                :tries 30}
                               #(ssh-instructions project build_num))]
    instructions))

(defn launch-ssh-connection [project]
  (let [{:keys [error out port username ip]} (get-ssh-instructions project)]
    (if error
      (errorf error)
      (do
        (infof out)
        (sh "osascript" :in (format "tell app \"Terminal\"
                                   do script \"ssh -p %s %s@%s -o StrictHostKeyChecking=no\"
                                   end tell"
                                    port username ip))))))

(defn -main
  [project]
  (let [exit (or (:exit (launch-ssh-connection project)) 1)]
    (System/exit exit)))
