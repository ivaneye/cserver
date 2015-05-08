(ns cserver.core
  (:require [cserver.cinitializer :refer :all]
            [cserver.chandler :as chandler])
  (:import (io.netty.bootstrap ServerBootstrap)
           (io.netty.channel.nio NioEventLoopGroup)
           (io.netty.channel.socket.nio NioServerSocketChannel NioSocketChannel)
           (cserver ServerInitializer)
           (io.netty.channel ChannelOption)
           (io.netty.handler.codec.http HttpResponseEncoder HttpRequestDecoder)))

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

(definit initialize (initChannel [^NioSocketChannel socketChannel]
                                 (let [pipeline (.pipeline socketChannel)]
                                   (.addLast pipeline nil (HttpRequestDecoder.))
                                   (.addLast pipeline nil (HttpResponseEncoder.))
                                   (.addLast pipeline "handler" (chandler/handler))
                                   (println "连接..."))))

(-> b (group bossGroup workerGroup)
    (channel (NioServerSocketChannel.))
    (child-handler initialize)
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