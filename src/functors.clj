;   Copyright (c) Jim Duey. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns functors
  (:refer-clojure)
  (:require [clojure.core.reducers :as r]))

(defprotocol Functor
  (fmap [coll f]))

(extend-type clojure.lang.PersistentList
  Functor
  (fmap [coll f]
    (list* (into [] (r/map f coll)))))

(extend-type clojure.lang.PersistentVector
  Functor
  (fmap [coll f]
    (into [] (r/map f coll))))

(deftype reader-f [v rf f]
  clojure.lang.IFn
  (invoke [_ e]
    (if rf
      (f (rf e))
      v))

  Functor
  (fmap [rf f]
    (reader-f. nil rf f)))

(defn reader [v]
  (reader-f. v nil nil))

(defn read-e [rf f]
  (fn [e]
    (f e (rf e))))
