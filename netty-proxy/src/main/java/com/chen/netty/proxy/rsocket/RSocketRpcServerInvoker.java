package com.chen.netty.proxy.rsocket;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import io.rsocket.DuplexConnection;
import io.rsocket.Payload;
import io.rsocket.util.ByteBufPayload;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

public class RSocketRpcServerInvoker implements RSocket {

	private DuplexConnection connection;

	public RSocketRpcServerInvoker(DuplexConnection connection) {
		this.connection = connection;
	}

	@Override
	public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
		Flux<Payload> flux = (Flux<Payload>) payloads;
		return flux.switchOnFirst((signal, source) -> {
			Payload payload = signal.get();
			Route route = getRouteMetadata(payload);

			ReferenceCountUtil.safeRelease(signal);
			RSocketMethodHandler handler = getServiceInvoker(route);
			try {
				return ((Flux<?>) handler.invokePayloads(source.skip(1)))
						.map(ProtobufCodec.INSTANCE::encode).map(ByteBufPayload::create)
						.subscriberContext(
								Context.of(RSocketRpc.RSOCKET_CONNECTION, connection));
			}
			catch (Exception e) {
				return Flux.error(e);
			}
		});
	}

	private Flux<Payload> requestChannel(Payload signal, Publisher<Payload> payloads) {
		ReferenceCountUtil.safeRelease(signal);

		Route route = getRouteMetadata(signal);

		RSocketMethodHandler handler = getServiceInvoker(route);

		try {
			return ((Flux<?>) handler.invokePayloads(Flux.from(payloads)))
					.map(ProtobufCodec.INSTANCE::encode).map(ByteBufPayload::create)
					.subscriberContext(
							Context.of(RSocketRpc.RSOCKET_CONNECTION, connection));
		}
		catch (Exception e) {
			return Flux.error(e);
		}
	}

	private RSocketMethodHandler getServiceInvoker(Route route) {
		final String serviceName = route.getService() + "#" + route.getMethod();
		return ProxyMethodHandlerRegistry.acquire(serviceName);
	}

	private Route getRouteMetadata(Payload signal) {
		ByteBuf data = signal.metadata();
		return ProtobufCodec.INSTANCE.decode(data, Route.class);
	}

	@Override
	public Mono<Payload> requestResponse(Payload payload) {
		ReferenceCountUtil.safeRelease(payload);

		Route route = ProtobufCodec.INSTANCE.decode(payload.metadata(), Route.class);
		RSocketMethodHandler handler = getServiceInvoker(route);

		try {
			return Mono.from((Mono<?>) handler.invokePayload(payload))
					.map(ProtobufCodec.INSTANCE::encode).map(ByteBufPayload::create)
					.subscriberContext(
							Context.of(RSocketRpc.RSOCKET_CONNECTION, connection));
		}
		catch (Exception exception) {
			Mono.error(exception);
		}

		return Mono.empty();
	}

	@Override
	public Flux<Payload> requestStream(Payload payload) {
		ReferenceCountUtil.safeRelease(payload);
		Route route = ProtobufCodec.INSTANCE.decode(payload.metadata(), Route.class);
		RSocketMethodHandler handler = getServiceInvoker(route);
		try {
			return ((Flux<?>) handler.invokePayload(payload))
					.map(ProtobufCodec.INSTANCE::encode).map(ByteBufPayload::create)
					.subscriberContext(
							Context.of(RSocketRpc.RSOCKET_CONNECTION, connection));
		}
		catch (Exception exception) {
			Flux.error(exception);
		}
		return Flux.empty();
	}

	@Override
	public Mono<Void> fireAndForget(Payload payload) {
		// todo.
		return null;
	}

}
