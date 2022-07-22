package com.chen.netty.proxy.test.server.http.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;


/**
 * @author chenwh
 * @date 2021/8/26
 */
@Slf4j
public class SimHttpResponseHandler extends SimpleChannelInboundHandler<FullHttpMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpMessage msg) throws Exception {
        log.info("receive =[{}] , type=[{}]", msg, msg.getClass());
        log.info("receive body = {}", msg.content().copy().toString(StandardCharsets.UTF_8));
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        ByteBuf byteBuf = Unpooled.copiedBuffer(StringUtils.repeat("123",100000), StandardCharsets.UTF_8);
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
        response.content().writeBytes(byteBuf);
        ctx.writeAndFlush(response);
    }

    public static void main(String[] args) {
        System.out.println(123);
        try {
            TimeUnit.SECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
