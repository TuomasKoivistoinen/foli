(ns foli.main
  (:require
    [nubank.workspaces.core :as ws]
    [foli.inputs]
    [foli.navigation]
    [foli.surfaces]
    [foli.feedback]
    [foli.data-display]
    [foli.design.login]))

(defonce init (ws/mount))
