
package com.chen.tls.tcp.client;

import com.chen.tls.utils.SslUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SNIHostName;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class NettyTcpSslClient {

    @Getter
    private ChannelFuture channel = null;

    @Setter
    private String host;

    @Setter
    private int port;




    public void connect() throws Exception {
        EventLoopGroup group = new
                NioEventLoopGroup();
        Bootstrap b = new Bootstrap();


        b.group(group).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE,
                true).handler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception{
                SSLEngine sslEngine = SslUtils.clientSslEngine("./temp.pem", "./temp.pem", ch.alloc());
                SSLParameters sslParameters = new SSLParameters();
                sslParameters.setServerNames(
                        Collections.singletonList(new SNIHostName("appName")));
                sslEngine.setSSLParameters(sslParameters);
                ch.pipeline().addLast(new SslHandler(sslEngine));
                ch.pipeline().addLast(new ClientHandler());
            }
        });

        channel = b.connect(new InetSocketAddress(host, port));

        channel.sync();

    }

    @ChannelHandler.Sharable
    static class ClientHandler extends ChannelDuplexHandler {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws
                Exception {
            ByteBuf byteBuf = (ByteBuf) msg;
            log.info("recieve = [{}]", byteBuf.toString(Charset.defaultCharset()));
        }


        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            log.info("", cause);
            super.exceptionCaught(ctx, cause);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("inactive ");
            super.channelInactive(ctx);
        }
    }

    public static void main(String[] args) throws Exception {
        NettyTcpSslClient nettyClient = new NettyTcpSslClient();
        CompletableFuture.runAsync(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String command = scanner.nextLine();
                log.info("send = [{}]", command);
                if (nettyClient.getChannel() != null) {
                    nettyClient.getChannel().channel().writeAndFlush(Unpooled.copiedBuffer((command +
                            "\r\n").getBytes(StandardCharsets.UTF_8)));
                }
            }
        });

        nettyClient.setHost("127.0.0.1");
        nettyClient.setPort(8997);
        nettyClient.connect();
    }

}

