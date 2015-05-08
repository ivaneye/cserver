(ns cserver.cinitializer
  (:import (io.netty.channel ChannelInitializer)))


(defmacro definit
  "创建ChannelInitializer子类，初始化Netty"
  [name & f]
  `(def ~name (proxy [ChannelInitializer] []
                ~@f)))




