package com.chen.test.sshd.support;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @author chenwh
 * @date 2021/11/12
 */

@Slf4j
public class EchoHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            log.info("read msg = {}", ((ByteBuf) msg).copy().toString(Charset.defaultCharset()));
        }else{
            log.info("read msg = {}", msg);
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("", cause);
        super.exceptionCaught(ctx, cause);
    }
}
