package com.chen.netty.proxy.test.server.http;

import com.chen.netty.proxy.test.server.Server;
import com.chen.netty.proxy.test.server.http.handler.SimHttpResponseHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.util.concurrent.TimeUnit;

/**
 * @author chenwh
 * @date 2021/8/26
 */

public class SimHttpServer implements Server {

    private int port = 8082;

    private ChannelFuture channelFuture = null;

    @Override
    public void create() throws InterruptedException {
        ServerBootstrap server = new ServerBootstrap();
        server.group(new NioEventLoopGroup(2), new NioEventLoopGroup(5))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //http协议的编解码器
                        ch.pipeline().addLast(new HttpServerCodec());
                        //聚合body
                        ch.pipeline().addLast(new HttpObjectAggregator(1024*1000*1000));
                        ch.pipeline().addLast(new SimHttpResponseHandler());
                    }
                })
                //整体配置
//                .option(ChannelOption.SO_BACKLOG, 200)
                //config after channel accepted
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        channelFuture = server.bind(port);

    }

    public static void main(String[] args) throws Exception{
        SimHttpServer simHttpServer = new SimHttpServer();
        simHttpServer.create();
        TimeUnit.SECONDS.sleep(1000);

    }


}
