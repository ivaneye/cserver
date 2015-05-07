(ns cserver.cinitializer
  (:require [cserver.chandler :as chandler])
  (:import (io.netty.channel ChannelInitializer)
           (io.netty.channel.socket.nio NioSocketChannel)
           (io.netty.handler.codec.http HttpRequestDecoder HttpResponseEncoder)
           (cserver ServerHandler)))


(defn initialize
  "创建ChannelInitializer子类，初始化Netty"
  []
  (proxy [ChannelInitializer] []
    (initChannel [^NioSocketChannel socketChannel]
      (let [pipeline (.pipeline socketChannel)]
        (.addLast pipeline nil (HttpRequestDecoder.))
        (.addLast pipeline nil (HttpResponseEncoder.))
        (.addLast pipeline "handler" (chandler/handler))
        (println "连接...")))))

