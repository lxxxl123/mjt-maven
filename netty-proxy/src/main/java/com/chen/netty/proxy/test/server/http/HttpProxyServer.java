package com.chen.netty.proxy.test.server.http;

import com.chen.netty.proxy.test.server.Server;
import com.chen.netty.proxy.test.server.http.handler.ProxyRequestHandler;
import com.chen.netty.proxy.test.server.http.handler.SimHttpResponseHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chenwh
 * @date 2021/8/26
 */
@Slf4j
public class HttpProxyServer implements Server {

    private int port = 8083;

    private ChannelFuture channelFuture = null;

    @Override
    public void create() throws InterruptedException {
        ServerBootstrap server = new ServerBootstrap();
        server.group(new NioEventLoopGroup(1), new NioEventLoopGroup(100))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addFirst(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                log.info("pipeline = {}", ctx.pipeline().names());

                            }
                        });
                        //http协议的编解码器
                        ch.pipeline().addLast(new HttpRequestDecoder());
//                        ch.pipeline().addLast("httpCodec",new HttpServerCodec());
                        //聚合body
                        ch.pipeline().addLast("httpObject",new HttpObjectAggregator(1024*1000*1000));
                        ch.pipeline().addLast(new ProxyRequestHandler());
                    }
                })
                //可连接队列
                .option(ChannelOption.SO_BACKLOG, 400)
                //客户端开启长连接的情况下需要服务端主动关闭连接
                .childOption(ChannelOption.SO_KEEPALIVE, true);


        channelFuture = server.bind(port);
    }

}
