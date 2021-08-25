package com.chen.netty.proxy.transmit.service;

public interface HttpProxyService {

	Flux<HttpProxyResponse> execute(Flux<HttpProxyRequest> flux);

}
