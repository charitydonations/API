(ns api.models.user
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [crypto.password.bcrypt :as password]
            [sendgrid-clj.core :as sg]
            [crypto.random :as random]
            [clj-stripe.common :as common]
            [clj-stripe.customers :as customers]
            [environ.core :as environ]))

(def db (mg/get-db "donations"))

(def stripe-connection (common/with-token (env :STRIPE_API_KEY)))

(declare get-user
         update-user)

(defn create [firstName lastName email password]
  (let [emailVerificationHash (random/base64)]
    (mc/insert db {:firstName firstName
                   :lastName lastName
                   :email email
                   :emailIsVerified false
                   :emailVerificationHash (random/base64 20)
                   :password (password/encrypt password)})

    (sg/send-email {:to email
                    :from "no-reply@donations.com"
                    :text (str "Verify your email: https://donations.com/verify/" emailVerificationHash)})))

(defn stripe [userId stripeToken]
  (let [user (get-user {:_id userId})]
    (->>  (customers/create-customer (common/card stripeToken)
                                     (customers/email (:email user)))
          (common/execute)
          (stripe-connection))))

(defn verify-email [userId verificationHash]
  (update-user {:_id userId verificationHash} {$set {:emailIsVerified true :emailVerificationHash nil}}))

;; HELPERS

(defn- get-user [query] (mc/find-one-as-map db "users" query))
(defn- update-user [query update] (mc/update db "users" query update))

