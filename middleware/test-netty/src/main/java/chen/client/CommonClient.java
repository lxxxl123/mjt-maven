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
import java.util.concurrent.CompletableFuture;

/**
 * @author chenwh
 * @date 2020/11/17
 */
@Slf4j
public class CommonClient implements Runnable {

    private ChannelHandler[] handlers ;

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
                        if (handlers != null) {
                            for (ChannelHandler handler : handlers) {
                                ch.pipeline().addLast(handler);
                            }
                        }else {
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    String msg = "成功与服务器建立连接";
                                    log.info("{}", msg);
                                    ByteBuf byteBuf = Unpooled.copiedBuffer(msg.getBytes());
                                    ctx.writeAndFlush(byteBuf);
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
            ChannelFuture future = b.connect(new InetSocketAddress("localhost", 8877)).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("",e);
        }finally {
            group.shutdownGracefully();
        }


    }

    public static void main(String[] args) {
        CompletableFuture.runAsync(new CommonClient()).join();
    }
}
