package com.chen.tls.handler;

import com.chen.tls.attr.Attr;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.AttributeKey;

import java.nio.charset.StandardCharsets;

public class EchoHandler extends ChannelInboundHandlerAdapter {



    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().attr(Attr.BUFFER).set(Unpooled.buffer(256, 10000));
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = ctx.channel().attr(Attr.BUFFER).get();
        if (msg instanceof ByteBuf) {
            ByteBuf bytebuf = (ByteBuf) msg;

            while (bytebuf.readableBytes() > 0) {
                byte b = ((ByteBuf) msg).readByte();
                buf.writeByte(b);
                if (b == '\n') {
                    ByteBuf res = Unpooled.buffer(256, 256);
                    res.writeBytes("echo : ".getBytes(StandardCharsets.UTF_8));
                    res.writeBytes(buf);
                    buf.clear();
                    ctx.writeAndFlush(res);
                }
            }
        }
    }
}