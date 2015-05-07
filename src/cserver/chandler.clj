(ns cserver.chandler
  (:import (io.netty.channel SimpleChannelInboundHandler ChannelHandlerContext)))


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
      (println "msg = " msg))
    ))