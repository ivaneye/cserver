(ns cserver.core
  (:import (io.netty.bootstrap ServerBootstrap)
           (io.netty.channel.nio NioEventLoopGroup)
           (io.netty.channel.socket.nio NioServerSocketChannel)
           (cserver ServerInitializer)
           (io.netty.channel ChannelOption)))

(def bossGroup (NioEventLoopGroup.))
(def workerGroup (NioEventLoopGroup. (int 4)))

(def b (ServerBootstrap.))

(.. b (group bossGroup workerGroup)
    (channel (class (NioServerSocketChannel.)))
    (childHandler (ServerInitializer.))
    (option ChannelOption/SO_BACKLOG (int 128))
    (childOption ChannelOption/SO_KEEPALIVE true))

(def f (.sync (.bind b (int 8080))))

(.sync (.closeFuture (.channel f)))