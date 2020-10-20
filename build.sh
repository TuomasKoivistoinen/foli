#! /bin/sh
npm i
shadow-cljs release main
clojure -A:depstar -m hf.depstar.uberjar Foli.jar