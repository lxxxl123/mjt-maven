package com.chen.netty.proxy.transmit.connector;

import com.chen.netty.proxy.model.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectorRegistry {

	private static final Logger LOG = LoggerFactory.getLogger(ConnectorRegistry.class);

	private static final Map<String, ReactiveConnector> registry = new ConcurrentHashMap<>();

	private static final Map<Protocol, Class<? extends ReactiveConnector>> connectorTypes = new HashMap<>();

	static {
		connectorTypes.put(Protocol.SFTP, SftpConnector.class);
		connectorTypes.put(Protocol.FTP, FtpConnector.class);
	}

	public static ReactiveConnector getOrAddConnector(final Config config) {
		String sessionId = config.getSessionId();
		return registry.computeIfAbsent(sessionId, i -> {
			final Protocol protocol = config.getProtocol();

			Class<? extends ReactiveConnector> clazz = connectorTypes.get(protocol);

			Objects.requireNonNull(clazz, protocol + " not support");

			try {
				return clazz.newInstance();
			}
			catch (Exception ex) {
				String msg = "Creates connector new instance error " + ex.getMessage();
				LOG.error(msg, ex);
				throw new RuntimeException(msg);
			}
		});
	}

}
