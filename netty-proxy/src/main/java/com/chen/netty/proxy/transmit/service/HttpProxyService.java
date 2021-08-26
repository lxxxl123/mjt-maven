package com.chen.netty.proxy.transmit.service;

import com.chen.netty.proxy.HttpProxyRequest;
import com.chen.netty.proxy.HttpProxyResponse;
import reactor.core.publisher.Flux;

public interface HttpProxyService {

	Flux<HttpProxyResponse> execute(Flux<HttpProxyRequest> flux);

}
