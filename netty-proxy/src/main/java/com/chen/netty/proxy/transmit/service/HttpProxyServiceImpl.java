package com.chen.netty.proxy.transmit.service;

import com.google.protobuf.ByteString;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelOption;
import io.netty.handler.codec.http.HttpMethod;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.netty.tcp.TcpClient;

import java.time.Duration;
import java.util.Objects;

public class HttpProxyServiceImpl{

	public Flux<HttpProxyResponse> execute(Flux<HttpProxyRequest> requestFlux) {
		return requestFlux.switchOnFirst((signal, source) -> {
			final HttpProxyRequest firstRequest = signal.get();
			Objects.requireNonNull(firstRequest, "firstRequest");

			final String host = firstRequest.getHost();
			final int port = firstRequest.getPort();

			return TcpClient.create().host(host).port(port)
					.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10_000)
					.option(ChannelOption.SO_KEEPALIVE, true).connect()
					.map(connection -> {
						// Http 直接发送第一个request, 未考虑chunk状态. 对于Https 第一个request用来握手建立连接.
						Flux<HttpProxyRequest> flux = Flux.concat(source);
						if (HttpMethod.CONNECT.name().equals(firstRequest.getMethod())) {
							flux = flux.skip(1);
						}
						flux.doOnComplete(() ->
						// Avoid disposeNow blocking the Non-Blocking Thread.
						Mono.fromRunnable(
								() -> connection.disposeNow(Duration.ofSeconds(5)))
								.subscribeOn(Schedulers.boundedElastic()).subscribe())
								.map(request -> Unpooled.wrappedBuffer(
										request.getPayload().toByteArray()))
								.concatMap(connection.outbound()::sendObject).subscribe();
						return connection;
					}).flatMapMany(
							connection -> connection.inbound().receive().map(byteBuf -> {
								byte[] bytes = ByteBufUtil.getBytes(byteBuf);
								return HttpProxyResponse.newBuilder()
										.setPayload(ByteString.copyFrom(bytes)).build();
							}));
		});
	}

}