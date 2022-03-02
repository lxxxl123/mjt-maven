package chen.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author chenwh
 * @date 2020/11/17
 */
@Slf4j
public class CommonClient implements Runnable {

    private static ChannelFuture channelFuture = null;

    private ChannelHandler[] handlers = null;

    public CommonClient(ChannelHandler... handlers) {
        this.handlers = handlers;
    }


    @Override
    public void run() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        if (handlers != null && handlers.length > 0) {
                            for (ChannelHandler handler : handlers) {
                                ch.pipeline().addLast(handler);
                            }
                        } else {
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    String msg = "成功与服务器建立连接";
                                    log.info("{}", msg);
                                    ctx.writeAndFlush(Unpooled.copiedBuffer("abcd\r\ncdef\r\n".getBytes()));
//                                    ctx.writeAndFlush(Unpooled.copiedBuffer("\r".getBytes()));c
//                                    ctx.writeAndFlush(Unpooled.copiedBuffer("\n".getBytes()));
                                }

                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    ByteBuf buf = (ByteBuf) msg;
                                    byte[] bytes = new byte[buf.readableBytes()];
                                    buf.readBytes(bytes);
                                    String receive = new String(bytes);
                                    log.info("成功读取服务端数据 = [{}]", receive);

                                }

                            });
                        }

                    }
                });

        try {
            ChannelFuture future = b.connect(new InetSocketAddress("localhost", 8877));
            channelFuture = future;
        } finally {

        }


    }

    /**
     * 与服务器连接并发送abcd
     * @param args
     */
    public static void main(String[] args) throws Exception {
        CompletableFuture.runAsync(new CommonClient()).join();
//        CompletableFuture.runAsync(() -> {
//            Channel channel = channelFuture.channel();
//            for (int i = 0; i < 10000; i++) {
//                channel.writeAndFlush(Unpooled.wrappedBuffer(("row:" + i + "\r\n").getBytes(StandardCharsets.UTF_8)));
//                try {
//                    TimeUnit.SECONDS.sleep(5);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).join();
        channelFuture.channel().closeFuture().sync();
    }
}
