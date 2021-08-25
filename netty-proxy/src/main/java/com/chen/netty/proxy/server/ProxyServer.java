package com.chen.netty.proxy.server;

import com.chen.netty.proxy.handler.HttpProxyHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * @author chenwh
 * @date 2021/8/25
 */

public class ProxyServer implements Server {

    // public static void main(String[] args) {
    // OrderHttpServer server = new OrderHttpServer();
    // server.start();
    // }

    private static final Logger LOG = LoggerFactory.getLogger(ProxyServer.class);

    private int port = 8083;

    ProxyServer(int port){
        this.port = port;

    }


    @Resource
    private HttpProxyHandler httpProxyHandler;

    private Channel channel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    @Override
    public void start() {

        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 10000);
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new HttpServerCodec());
                            // 10m最大请求大小
                            p.addLast(new HttpObjectAggregator(1_048_576 * 10));
                            p.addLast(httpProxyHandler);
                            p.addLast(new IdleStateHandler(0, 0, 60));
                        }
                    });

            channel = b.bind(port).sync().channel();
        } catch (InterruptedException ignore) {
            System.exit(-1);
        }
        LOG.info("启动Http代理服务成功,服务端口:[{}].", port);
    }

    @Override
    public void stop() throws Exception {
        try {
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            // ignored
        } finally {
            if (bossGroup != null) {
                bossGroup.shutdownGracefully();
            }
            if (workerGroup != null) {
                workerGroup.shutdownGracefully();
            }
        }
    }

}

