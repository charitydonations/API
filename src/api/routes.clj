(ns api.routes
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [api.models.user :as user])
  (:import [org.bson.types ObjectId]
           [com.mongodb DB WriteConcern]))

(defroutes routes
  (context "/api" []
    (POST "/user" [firstName lastName password email]
          (user/create firstName lastName email password))

    (POST "/users/:userId/verify-email" [userId emailVerificationHash]
          (user/verify-email userId emailVerificationHash))

    (POST "/users/:userId/stripe" [userId stripeToken]
          (user/stripe userId stripeToken))

    (GET "/institutions" [])

    (POST "/users/:userId/institution" [userId institutionId username password]
          (user/institution institutionId username password))

    (route/resources "/")
    (route/not-found "Page not found"))

(def app
  (-> (handler/site routes)
      (wrap-base-url)))
