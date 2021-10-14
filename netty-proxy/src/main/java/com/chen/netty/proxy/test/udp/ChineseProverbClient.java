package com.chen.netty.proxy.test.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

public class ChineseProverbClient {


    public class ChineseProverbClientHandler extends
            SimpleChannelInboundHandler<DatagramPacket> {
        @Override
        public void channelRead0(ChannelHandlerContext ctx, DatagramPacket
                msg) throws Exception {
            String response = msg.content().toString(CharsetUtil.UTF_8);
            if (response.startsWith("谚语查询结果：")) {
                System.out.println(response);
                ctx.close();
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable
                cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }

    }

    public void run(int port) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChineseProverbClientHandler());
            Channel ch = b.bind(0).sync().channel();
            //向网段内的所有机器广播
            ch.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
                    "谚语字典查询?", CharsetUtil.UTF_8), new InetSocketAddress(
                    "255.255.255.255", port))).sync();
            //客户端等待15s用于接收服务端的应答消息，然后退出并释放资源
            if (!ch.closeFuture().await(15000)) {
                System.out.println("查询超时！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8888;
        if (args != null && args.length > 0) {
            port = Integer.valueOf(args[0]);
        }
        new ChineseProverbClient().run(port);
    }
}