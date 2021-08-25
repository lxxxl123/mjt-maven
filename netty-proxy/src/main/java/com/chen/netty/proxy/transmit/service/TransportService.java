package com.chen.netty.proxy.transmit.service;

public interface TransportService {

	Flux<Response> connect(ConnectRequest request);

	Flux<Response> execute(Flux<Request> requestFlux);

	Flux<Response> disconnect(DisconnectRequest connectRequest);

}