package com.chen.netty.proxy.transmit.service;

import com.chen.netty.proxy.model.Config;
import com.chen.netty.proxy.model.Response;
import com.chen.netty.proxy.transmit.connector.ConnectorRegistry;
import com.chen.netty.proxy.transmit.connector.ReactiveConnector;
import com.google.protobuf.ByteString;
import com.sun.jdi.connect.spi.TransportService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.Objects;

public class TransportServiceImpl implements TransportService {

	private static final Logger LOG = LoggerFactory.getLogger(TransportServiceImpl.class);

	@Override
	public Flux<Response> connect(ConnectRequest request) {
		Config config = Config.build(request);
		ReactiveConnector connector = ConnectorRegistry.getOrAddConnector(config);
		connector.init(config);
		return Flux.from(connector.connect()).doOnError(Throwable::printStackTrace);
	}

	@Override
	public Flux<Response> execute(Flux<Request> requestFlux) {
		return requestFlux.switchOnFirst((signal, source) -> {
			Request request = signal.get();
			Objects.requireNonNull(request, "request must be not null");

			// First element is Command Request.
			Flux<ByteBuf> flux = source.skip(1)
					.map(r -> Unpooled.wrappedBuffer(r.getData().toByteArray()));

			Config config = Config.build(request);
			ReactiveConnector connector = ConnectorRegistry.getOrAddConnector(config);
			return connector.execute(request, flux)
					.subscribeOn(Schedulers.boundedElastic());
		}).map(byteBuf -> Response.newBuilder()
				.setData(ByteString.copyFrom(byteBuf.array())).build());
	}

	@Override
	public Flux<Response> disconnect(DisconnectRequest request) {
		Config config = Config.build(request);
		ReactiveConnector connector = ConnectorRegistry.getOrAddConnector(config);
		return connector.disconnect().thenMany(Flux.empty());
	}

}
