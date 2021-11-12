package com.chen.test.sshd.support;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.sshd.common.io.IoHandler;
import org.apache.sshd.common.session.helpers.AbstractSession;
import org.apache.sshd.netty.NettyIoService;
import org.apache.sshd.netty.NettyIoSession;

import java.net.SocketAddress;

/**
 * @author chenwh
 * @date 2021/11/12
 */

public class MyNettyIoSession extends NettyIoSession {
    public MyNettyIoSession(NettyIoService service, IoHandler handler, SocketAddress acceptanceAddress) {
        super(service, handler, acceptanceAddress);
    }
    public ChannelInboundHandlerAdapter getAdaper(){
        return new Adapter();
    }


    protected class Adapter extends ChannelInboundHandlerAdapter {
        public Adapter() {
            super();
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            MyNettyIoSession.this.channelActive(ctx);
            ctx.fireChannelActive();
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            MyNettyIoSession.this.channelInactive(ctx);
            ctx.fireChannelInactive();
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            MyNettyIoSession.this.channelRead(ctx, msg);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            MyNettyIoSession.this.exceptionCaught(ctx, cause);
            ctx.fireExceptionCaught(cause);
        }
    }
}
