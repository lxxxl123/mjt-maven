package com.chen.netty.proxy.test.server.http.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author chenwh
 * @date 2021/8/26
 */
@Slf4j
public class ProxyRequestHandler extends ChannelInboundHandlerAdapter {

    public static NioEventLoopGroup group = new NioEventLoopGroup(300);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("receive =[{}] , type=[{}]", msg, msg.getClass());
        if (!(msg instanceof FullHttpRequest)) {
            return;
        }
        FullHttpRequest res = ((FullHttpRequest) msg);
        String[] host_port = res.headers().get("host").split(":");
        ctx.pipeline().remove("httpCodec");
        ctx.pipeline().remove("httpObject");
        if (HttpMethod.CONNECT.equals(res.method())) {
            HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            ctx.writeAndFlush(response);
        } else {
            try {
                String remoteHost = host_port[0];
                int remotePort = Integer.parseInt(host_port[1]);
                sendWithHttpClient(remoteHost, remotePort, ctx, res);
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }


    public void sendWithHttpClient(String ip, int port, ChannelHandlerContext ctx, FullHttpRequest msg) {
        EmbeddedChannel ch = new EmbeddedChannel(new HttpRequestEncoder());
        ch.writeOutbound(msg);
        ByteBuf byteBuf = ch.readOutbound();
//        ctx.pipeline().remove("httpCodec");
        AtomicReference<Connection> conn = new AtomicReference<>();

        Connection connection = TcpClient.create()
                .host(ip).port(port)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                .handle((in, out) -> out.sendObject(byteBuf).then(
                        in.receive().flatMap(receive -> {
                            receive.retain();
                            ctx.writeAndFlush(receive).addListener(li -> {
                                if (li.isSuccess()) {
                                    ctx.close();
                                } else {
                                    log.error("write fail");
                                }
                                conn.get().dispose();
                            });
                            return Mono.empty();
                        }))
                ).connectNow();
        conn.set(connection);

    }

    public void sendHttpWithNetty(String ip, int port, ChannelHandlerContext ctx, Object msg) {
        String remoteHost = ip;
        int remotePort = port;

        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture fu = null;
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new HttpClientCodec());
                        //聚合body
                        ch.pipeline().addLast(new HttpObjectAggregator(1024 * 1000 * 1000));
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx1, Object msg) throws Exception {
                                if (msg instanceof FullHttpResponse) {
                                    ((FullHttpResponse) msg).headers().set("from", "proxy");
                                    ctx.writeAndFlush(msg);
                                    ctx1.close().sync();
                                }
                            }
                        });

                    }
                });
        fu = bootstrap.connect(remoteHost, remotePort);
        fu.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                future.channel().writeAndFlush(msg).sync();
            } else {
                future.channel().close();
            }
        });
    }

}
