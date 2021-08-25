package com.chen.netty.proxy.rsocket;

import io.rsocket.DuplexConnection;

/**
 *
 * @author echooymxq
 **/
public interface ConnectionListener {

	default void connectionClosed(DuplexConnection	connection) {

	}

	default void connectionCreated(DuplexConnection connection) {

	}

}
