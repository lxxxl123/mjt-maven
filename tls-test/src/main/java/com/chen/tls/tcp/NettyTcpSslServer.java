package com.chen.tls.tcp;

import com.chen.tls.handler.EchoHandler;
import com.chen.tls.https.Server;
import com.chen.tls.utils.SslUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * @author chenwh
 * @date 2021/8/31
 */

public class NettyTcpSslServer implements Server {

    public int port = 8997;


    @Override
    public void create() throws Exception {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .group(boss, worker)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            SSLEngine sslEngine = SslUtils.sslEngine("./temp.pem", "./temp.pem", ch.alloc());
                            sslEngine.setUseClientMode(false);
                            ch.pipeline().addLast(new SslHandler(sslEngine));
                            ch.pipeline().addLast(new EchoHandler());
                        }
                    });
            ChannelFuture future = serverBootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws Exception {
        new NettyTcpSslServer().create();
    }

}