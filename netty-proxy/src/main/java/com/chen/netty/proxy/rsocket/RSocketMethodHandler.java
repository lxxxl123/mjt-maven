package com.chen.netty.proxy.rsocket;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import io.rsocket.Payload;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class RSocketMethodHandler {

	private Object proxy;

	private Method method;

	private Class<?> parameterType;

	public RSocketMethodHandler(Object proxy, Method method) {
		this.proxy = proxy;
		this.method = method;
		init();
	}

	private void init() {
		this.method.setAccessible(true);
		Class<?>[] parameterTypes = method.getParameterTypes();
		if (parameterTypes.length > 0) {
			Type[] genericParameterTypes = method.getGenericParameterTypes();
			Type type = genericParameterTypes[parameterTypes.length - 1];
			if (type instanceof ParameterizedType) {
				parameterType = (Class<?>) ((ParameterizedType) type)
						.getActualTypeArguments()[0];
			}
			else {
				parameterType = (Class<?>) type;
			}
		}
	}

	public Object invokePayloads(Flux<Payload> payloads) throws Exception {
		if (parameterType != null) {
			Flux<?> params = payloads.map(payload -> {
				ReferenceCountUtil.safeRelease(payload);
				return payload.data();
			}).flatMap(byteBuf -> Mono
					.just(ProtobufCodec.INSTANCE.decode(byteBuf, parameterType)));

			return this.method.invoke(proxy, params);
		}
		return null;
	}

	public Object invokePayload(Payload payload) throws Exception {
		if (parameterType != null) {
			ByteBuf byteBuf = payload.data();

			Object params = ProtobufCodec.INSTANCE.decode(byteBuf, parameterType);
			return this.method.invoke(proxy, params);
		}
		return null;
	}

}
