package chen.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.LineEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;


/**
 * @author chenwh
 * @date 2020/11/17
 */
@Slf4j
public class CommonServer {

    private ChannelHandler[] handlers ;

    public CommonServer(ChannelHandler... handlers) {
        this.handlers = handlers;
    }

    public void run() {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap server = new ServerBootstrap();
        server.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .childHandler(new LineBasedFrameDecoder(2048,true,false))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        if (handlers != null && handlers.length > 0) {
                            for (ChannelHandler handler : handlers) {
                                ch.pipeline().addLast(handler);
                            }
                        } else {
                            ch.pipeline().addLast(new TimerHandler());
                        }
                    }
                });


        try {
            ChannelFuture future = server.bind(new InetSocketAddress("localhost", 8877)).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e){
            log.error("", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }


    }

    private static class TimerHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf buf = (ByteBuf) msg;
            byte [] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            log.info("接收到bytes = [{}]", Arrays.toString(bytes));
            String receive = new String(bytes);
            log.info("接收到消息 = [{}]", receive);

        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
             ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            log.error("",cause);
            ctx.close();
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            log.error("服务端断连接");
        }
    }

    public static void main(String[] args) {

    }
}
