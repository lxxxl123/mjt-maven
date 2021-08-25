package com.chen.netty.proxy.transmit.connector;

import com.chen.netty.proxy.model.Config;
import io.netty.buffer.ByteBuf;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author echooymxq
 **/
public interface ReactiveConnector {

	default void init(Config config) {
	}

	Mono<Response> connect();

	Flux<ByteBuf> execute(Request request, Flux<ByteBuf> source);

	Mono<Void> disconnect();

	boolean isConnected();

}