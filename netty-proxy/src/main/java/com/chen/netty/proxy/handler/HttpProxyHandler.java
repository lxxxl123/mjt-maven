package com.chen.netty.proxy.handler;

import com.chen.netty.proxy.HttpProxyRequest;
import com.chen.netty.proxy.transmit.service.HttpProxyService;
import com.google.protobuf.ByteString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleState;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @author chenwh
 * @date 2021/8/25
 */

@Service
@ChannelHandler.Sharable
public class HttpProxyHandler extends ChannelInboundHandlerAdapter {

    private static final AttributeKey<Boolean> TUNNELING = AttributeKey.valueOf("TUNNELING");

    private static final AttributeKey<Sinks.Many<HttpProxyRequest>> HTTP_SINKS = AttributeKey.valueOf("HTTP_SINKS");

    private static final Logger LOG = LoggerFactory.getLogger(HttpProxyHandler.class);

    static String remoteAddress(ChannelHandlerContext ctx) {
        return ctx.channel().remoteAddress().toString();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().attr(TUNNELING).set(false);
        ctx.channel().attr(HTTP_SINKS)
                .set(Sinks.many().unicast().onBackpressureBuffer());
        LOG.info("Connection[{}]建立.", remoteAddress(ctx));
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        boolean tunneling = ctx.channel().attr(TUNNELING).get();
        Sinks.Many<HttpProxyRequest> sinks = ctx.channel()
                .attr(HTTP_SINKS).get();
        // http chunk ?
        if (tunneling) {
            LOG.info("[{}] transport data with tunnel.", remoteAddress(ctx));
            targetTunnel((ByteBuf) msg, sinks);
        }
        else {
            handleRequest(ctx, msg);
        }
    }

    private void handleRequest(ChannelHandlerContext ctx, Object msg) throws Exception {
        HttpRequest request = (HttpRequest) msg;

        boolean authSuccess = authenticate(ctx, request);

        if (authSuccess) {
            LOG.info("[{}] 认证成功，执行连接代理操作.", remoteAddress(ctx));
            connectProxy(ctx, request);
        }
    }

    /**
     * 这步是必须的，当代理连接请求头中无proxy-authorization，必须返回 PROXY_AUTHENTICATION_REQUIRED
     */
    private static void requireAuthorization(ChannelHandlerContext ctx) {
        HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.PROXY_AUTHENTICATION_REQUIRED);
        response.headers().set(HttpHeaderNames.PROXY_AUTHENTICATE, "Basic realm=\"\"");
        ctx.writeAndFlush(response);
    }

    private static final Scheduler HTTP_PROXY_THREAD_GROUPS = Schedulers
            .newBoundedElastic(200, Integer.MAX_VALUE, "thread-http-proxy", 5);

    private void connectProxy(ChannelHandlerContext ctx, HttpRequest request)
            throws Exception {
        Sinks.Many<HttpProxyRequest> sinks = ctx.channel()
                .attr(HTTP_SINKS).get();

        // 移除代理相关头
        request.headers().remove(HttpHeaderNames.PROXY_AUTHORIZATION);
        request.headers().remove(HttpHeaderNames.PROXY_CONNECTION);

        HttpProxyRequest httpProxyRequest = transform(request);
        // 此处的host相当于设备ip，查询资源服务得到采集机ip.
        serviceDiscovery
                // 获取可用采集机
                .getFilteredInstances(DispatcherBootstrap.proxyRpcServiceName,
                        resourceRepository.getProviderAddress(httpProxyRequest.getHost()))
                // 构建http请求
                .map(instance -> new URL(instance.getHost(), instance.getPort()))
                .map(url -> ServiceReference.refer(HttpProxyService.class, url))
                // 发送http请求
                .flatMap(httpProxyService -> httpProxyService
                        .execute(Flux.just(httpProxyRequest).concatWith(sinks.asFlux())))
                .subscribeOn(HTTP_PROXY_THREAD_GROUPS).doOnError(e -> {
            exceptionCaught(ctx, e);
            ctx.channel().close();
        }).subscribe(response -> {
            byte[] bytes = response.getPayload().toByteArray();
            ctx.writeAndFlush(Unpooled.wrappedBuffer(bytes));
        });

        // Https Connect请求后，需要服务端返回200, 后续进行TLS协商.
        if (HttpMethod.CONNECT.equals(request.method())) {
            ctx.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK));
        }

        setTunnelStatus(ctx);
    }

    /**
     * 透传Packets
     */
    private void targetTunnel(ByteBuf byteBuf, Sinks.Many<HttpProxyRequest> sinks) {
        byte[] data = ByteBufUtil.getBytes(byteBuf);
        HttpProxyRequest proxyRequest = HttpProxyRequest.newBuilder()
                .setPayload(ByteString.copyFrom(data)).build();
        ReferenceCountUtil.safeRelease(byteBuf);
        sinks.emitNext(proxyRequest, (signalType, emitResult) -> {
            LOG.error("targetTunnel fail , type = {} result = {}", signalType,
                    emitResult);
            return false;
        });
    }

    /**
     * 设置为隧道状态，移除http编解码器，透传后续Packets.
     */
    private void setTunnelStatus(ChannelHandlerContext ctx) {
        ctx.channel().attr(TUNNELING).set(true);
        ctx.pipeline().remove(HttpServerCodec.class);
        ctx.pipeline().remove(HttpObjectAggregator.class);
    }

    /**
     * 认证代理连接请求.
     */
    private boolean authenticate(ChannelHandlerContext ctx, HttpRequest request) {
        String authorization = request.headers().get(HttpHeaderNames.PROXY_AUTHORIZATION);

        if (StringUtils.isEmpty(authorization)) {
            requireAuthorization(ctx);
            return false;
        }
        String value = StringUtils.substringAfter(authorization, "Basic ").trim();
        byte[] decodedAuthBytes = Base64Utils.decode(value.getBytes(StandardCharsets.UTF_8));
        String decodedAuth = new String(decodedAuthBytes, CharsetUtil.US_ASCII);
        String username = StringUtils.substringBefore(decodedAuth, ":");
        String password = StringUtils.substringAfter(decodedAuth, ":");

        boolean isSuccess = userAuthenticateService.login(username, password).isSuccess();

        if (!isSuccess) {
            authFailure(ctx);
        }
        return isSuccess;
    }

    /**
     * @param url url
     * @return 1-host , 2-port
     */
    public static Tuple2<String, String> getHostAndPortFromUrl(String url) {
        int idx = url.lastIndexOf(':');
        Assert.isTrue(idx != -1 && idx != url.length() - 1, "http请求url有误");
        return Tuples.of(url.substring(0, idx), url.substring(idx + 1));
    }

    private static void authFailure(ChannelHandlerContext ctx) {
        HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.PROXY_AUTHENTICATION_REQUIRED);
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);
        ctx.writeAndFlush(response);
    }

    public static HttpProxyRequest transform(HttpRequest request) throws Exception {
        // 根据url获取真实host和port
        Tuple2<String, String> url = getHostAndPortFromUrl(request.headers().get("host"));
        final String host = url.getT1();
        final int port = Integer.parseInt(url.getT2());

        EmbeddedChannel ch = new EmbeddedChannel(new HttpRequestEncoder());
        ch.writeOutbound(request);
        ByteBuf byteBuf = ch.readOutbound();
        HttpProxyRequest proxyRequest = HttpProxyRequest.newBuilder().setHost(host)
                .setPort(port).setMethod(request.method().name())
                .setPayload(ByteString.copyFrom(ByteBufUtil.getBytes(byteBuf))).build();
        ReferenceCountUtil.safeRelease(byteBuf);
        return proxyRequest;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        serverErrorResponse(ctx, cause);
        LOG.error("服务端异常", cause);
    }

    private static void serverErrorResponse(ChannelHandlerContext ctx, Throwable cause) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.INTERNAL_SERVER_ERROR, Unpooled.wrappedBuffer(
                cause.getLocalizedMessage().getBytes(StandardCharsets.UTF_8)));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH,
                response.content().readableBytes());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Sinks.Many<HttpProxyRequest> sinks = ctx.channel()
                .attr(HTTP_SINKS).get();
        LOG.info("Connection[{}]关闭.", remoteAddress(ctx));
        // 发送关闭连接请求.
        sinks.emitComplete((signalType, emitResult) -> {
            LOG.error("channelInactive fail , type = {} result = {}", signalType,
                    emitResult);
            return false;
        });
        super.channelInactive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {
        Sinks.Many<HttpProxyRequest> sinks = ctx.channel()
                .attr(HTTP_SINKS).get();
        // 空闲超时，关闭proxy测与设备的Tcp连接.
        if (evt == IdleState.ALL_IDLE) {
            sinks.tryEmitComplete();
        }
        super.userEventTriggered(ctx, evt);
    }
}