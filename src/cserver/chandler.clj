(ns cserver.chandler
  (:import (io.netty.channel SimpleChannelInboundHandler ChannelHandlerContext ChannelFutureListener)
           (io.netty.handler.codec.http HttpRequest LastHttpContent)
           (io.netty.buffer Unpooled)))

(defn do-request [ctx msg]
  (.write ctx msg))

(defn do-content [ctx msg]
  (if (instance? LastHttpContent msg)
    (.addListener (.writeAndFlush ctx Unpooled/EMPTY_BUFFER) ChannelFutureListener/CLOSE))
  (println "Content = " msg)
  (println "---------------------------------------"))

(defn handler
  "创建SimpleChannelInboundHandler子类，处理请求"
  []
  (proxy [SimpleChannelInboundHandler] []
    (channelReadComplete [^ChannelHandlerContext ctx]
      (.flush ctx))
    (channelActive [^ChannelHandlerContext ctx]
      (let [incoming (.channel ctx)]
        (println "Brower "  incoming  "接入")))
    (channelInactive [^ChannelHandlerContext ctx]
      (let [incoming (.channel ctx)]
        (println "Brower "  incoming  "断开")))
    (exceptionCaught [^ChannelHandlerContext ctx ^Throwable cause]
      (let [incoming (.channel ctx)]
        (println "Brower "  incoming  "异常"))
      (.printStackTrace cause)
      (.close ctx))
    (channelRead0 [^ChannelHandlerContext ctx msg]
      (if (instance? HttpRequest msg) (do-request ctx msg) (do-content ctx msg)))
    ))