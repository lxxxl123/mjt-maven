package com.chen.netty.proxy.transmit;

import com.chen.netty.proxy.rsocket.RSocketMethodHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyMethodHandlerRegistry {

	private static final Map<String, RSocketMethodHandler> methodHandlers = new ConcurrentHashMap<>();

	public static void registry(String serviceName, RSocketMethodHandler invoker) {
		methodHandlers.put(serviceName, invoker);
	}

	public static RSocketMethodHandler acquire(String serviceName) {
		return methodHandlers.get(serviceName);
	}

}
