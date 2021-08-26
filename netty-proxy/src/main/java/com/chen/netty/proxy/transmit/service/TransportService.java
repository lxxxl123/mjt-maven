package com.chen.netty.proxy.transmit.service;

import com.chen.netty.proxy.client.ConnectRequest;
import com.chen.netty.proxy.client.DisconnectRequest;
import com.chen.netty.proxy.client.Request;
import com.chen.netty.proxy.client.Response;
import reactor.core.publisher.Flux;

public interface TransportService {

	Flux<Response> connect(ConnectRequest request);

	Flux<Response> execute(Flux<Request> requestFlux);

	Flux<Response> disconnect(DisconnectRequest connectRequest);

}