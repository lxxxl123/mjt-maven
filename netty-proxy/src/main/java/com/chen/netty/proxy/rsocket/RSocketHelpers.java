package com.chen.netty.proxy.rsocket;

import io.rsocket.DuplexConnection;
import io.rsocket.RSocket;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

public class RSocketHelpers {

	public static String getRemoteAddress(RSocket sendingSocket) {

		Field connectionField = ReflectionUtils.findField(sendingSocket.getClass(),
				"connection");
		if (connectionField != null) {
			connectionField.setAccessible(true);
			DuplexConnection connection = (DuplexConnection) ReflectionUtils
					.getField(connectionField, sendingSocket);
			return connection.remoteAddress().toString();
		}
		return null;
	}

	public static DuplexConnection getConnection(RSocket sendingSocket) {

		Field connectionField = ReflectionUtils.findField(sendingSocket.getClass(),
				"connection");
		if (connectionField != null) {
			connectionField.setAccessible(true);
			return (DuplexConnection) ReflectionUtils.getField(connectionField,
					sendingSocket);
		}
		return null;
	}

}
