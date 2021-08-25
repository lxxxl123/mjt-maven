package com.chen.netty.proxy.server;

import com.chen.netty.proxy.rsocket.ConnectionListener;
import com.chen.netty.proxy.rsocket.RSocketHelpers;
import com.chen.netty.proxy.rsocket.RSocketMethodHandler;
import com.chen.netty.proxy.rsocket.RSocketRpcServerInvoker;
import io.rsocket.DuplexConnection;
import io.rsocket.core.RSocketServer;
import io.rsocket.plugins.DuplexConnectionInterceptor;
import io.rsocket.transport.netty.server.CloseableChannel;
import io.rsocket.transport.netty.server.TcpServerTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.netty.tcp.TcpServer;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Stream;

public class RpcServer {

	private static final Logger LOG = LoggerFactory.getLogger(RpcServer.class);

	private final Collection<DuplexConnectionInterceptor> connectionInterceptors = new CopyOnWriteArraySet<>();

	private final Collection<ConnectionListener> connectionListeners = new CopyOnWriteArraySet<>();

	private String host;
	private int port;
	private int idleTimeout;
	private CloseableChannel closeableChannel;

	public static RpcServer create() {
		return new RpcServer();
	}

	public static RpcServer create(String host, int port) {
		RpcServer server = new RpcServer();
		server.host = host;
		server.port = port;
		return server;
	}

	public RpcServer host(String host) {
		this.host = host;
		return this;
	}

	public RpcServer port(int port) {
		this.port = port;
		return this;
	}

	public RpcServer idleTimeout(final int idleTimeout) {
		this.idleTimeout = idleTimeout;
		return this;
	}

	public RpcServer addConnectionInterceptor(
			DuplexConnectionInterceptor connectionInterceptor) {
		connectionInterceptors.add(connectionInterceptor);
		return this;
	}

	public RpcServer addConnectionListener(ConnectionListener connectionListener) {
		connectionListeners.add(connectionListener);
		return this;
	}

	private void connectionCreated(DuplexConnection connection) {
		for (ConnectionListener listener : connectionListeners) {
			listener.connectionCreated(connection);
		}
	}

	private void connectionClosed(DuplexConnection connection) {
		for (ConnectionListener listener : connectionListeners) {
			listener.connectionClosed(connection);
		}
	}

	public <T> RpcServer addService(Class<T> clazz, T service) {
		Stream.of(clazz.getMethods()).forEach(method -> {
			String serviceName = clazz.getCanonicalName() + "#" + method.getName();
			ProxyMethodHandlerRegistry.registry(serviceName,
					new RSocketMethodHandler(service, method));
		});
		return this;
	}

	public RpcServer start() {
		TcpServer tcpServer = TcpServer.create().doOnConnection(connection -> {
		}).port(port);

		TcpServerTransport transport = TcpServerTransport.create(tcpServer);

		RSocketServer rSocketServer = RSocketServer.create();

		for (DuplexConnectionInterceptor connectionInterceptor : connectionInterceptors) {
			rSocketServer.interceptors(interceptorRegistry -> {
				interceptorRegistry.forConnection(connectionInterceptor);
			});
		}

		closeableChannel = rSocketServer.acceptor((setup, sendingSocket) -> {
			DuplexConnection connection = RSocketHelpers.getConnection(sendingSocket);
			connectionCreated(connection);

			sendingSocket.onClose().subscribe(unused -> {
				// todo
			}, throwable -> {
			}, () -> connectionClosed(connection));
			return Mono.just(new RSocketRpcServerInvoker(connection));
		}).bindNow(transport);
		return this;
	}

	public void stop() {
		if (closeableChannel != null && !closeableChannel.isDisposed()) {
			closeableChannel.dispose();
		}
	}
}
