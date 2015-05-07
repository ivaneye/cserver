(ns cserver.core
  (:require [cserver.cinitializer :as init])
  (:import (io.netty.bootstrap ServerBootstrap)
           (io.netty.channel.nio NioEventLoopGroup)
           (io.netty.channel.socket.nio NioServerSocketChannel)
           (cserver ServerInitializer)
           (io.netty.channel ChannelOption)))

(defmacro defgroup [name & args]
  (if args
    `(def ~name (NioEventLoopGroup. (int ~@args)))
    `(def ~name (NioEventLoopGroup.))))

;(defn create-group
;  ([] (NioEventLoopGroup.))
;  ([n] (NioEventLoopGroup. (int n))))

(defmacro defserver [name]
  `(def ~name (ServerBootstrap.)))

(defgroup bossGroup)
(defgroup workerGroup 4)


(defserver b)

(defn group [s boss worker]
  (.group s boss worker))

(defn channel [s channel]
  (.channel s (class channel)))

(defn child-handler [s init]
  (.childHandler s init))

(defn option [s option n]
  (.option s option (int n)))

(defn child-option [s option b]
  (.childOption s option b))


;(def bossGroup (create-group))
;(def workerGroup (create-group 4))

;(def b (server-bootstrap))

(-> b (group bossGroup workerGroup)
    (channel (NioServerSocketChannel.))
    (child-handler (init/initialize))
    (option ChannelOption/SO_BACKLOG 128)
    (child-option ChannelOption/SO_KEEPALIVE true))

;(.. b (group bossGroup workerGroup)
;    (channel (class (NioServerSocketChannel.)))
;    (childHandler (init/initialize))
;    (option ChannelOption/SO_BACKLOG (int 128))
;    (childOption ChannelOption/SO_KEEPALIVE true))

(defn bind [s port]
  (.bind s (int port)))

(defn sync [s]
  (.sync s))

(defn channel [f]
  (.channel f))

(defn close-future [f]
  (.closeFuture f))

(def f (sync (bind b 8080)))

(sync (close-future (channel f)))