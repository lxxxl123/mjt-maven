package com.chen.netty.proxy.test.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author chenwh
 * @date 2021/10/14
 */

public class ChineseProverbServer {

    private static final String[] DICTIONARY = {"小葱拌豆腐，一穷二白", "只要功夫深，铁棒磨成针", "山中无老虎，猴子称霸王"};

    class ChineseProverbServerHandler extends
            SimpleChannelInboundHandler<DatagramPacket> {

        private String nextQuote() {
            //线程安全岁基类，避免多线程环境发生错误
            int quote = ThreadLocalRandom.current().nextInt(DICTIONARY.length);
            return DICTIONARY[quote];
        }

        //接收Netty封装的DatagramPacket对象，然后构造响应消息
        @Override
        protected void channelRead0(ChannelHandlerContext ctx
                , DatagramPacket packet) throws Exception {
            //利用ByteBuf的toString()方法获取请求消息
            String req = packet.content().toString(CharsetUtil.UTF_8);
            System.out.println(req);
            if ("谚语字典查询?".equals(req)) {
                //创建新的DatagramPacket对象，传入返回消息和目的地址（IP和端口）
                ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
                        "谚语查询结果：" + nextQuote(), CharsetUtil.UTF_8), packet.sender()));
            }
        }


        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
                throws Exception {
            ctx.close();
            cause.printStackTrace();
        }
    }


    public void run(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        try {
            //通过NioDatagramChannel创建Channel，并设置Socket参数支持广播
            //UDP相对于TCP不需要在客户端和服务端建立实际的连接，因此不需要为连接（ChannelPipeline）设置handler
            Bootstrap b = new Bootstrap();
            b.group(bossGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChineseProverbServerHandler());
            b.bind(port).sync().channel().closeFuture().await();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8888;
        if (args != null && args.length > 0) {
            port = Integer.valueOf(args[0]);
        }
        new ChineseProverbServer().run(port);
    }
}