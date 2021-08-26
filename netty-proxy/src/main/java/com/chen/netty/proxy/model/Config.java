package com.chen.netty.proxy.model;

import com.chen.netty.proxy.client.ConnectRequest;
import com.chen.netty.proxy.client.DisconnectRequest;
import com.chen.netty.proxy.client.Protocol;
import com.chen.netty.proxy.client.Request;

import java.util.Map;

public class Config {

	private String sessionId;
	private Protocol protocol;
	private Map<String, String> params;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Protocol getProtocol() {
		return protocol;
	}

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public static Config build(final ConnectRequest request) {
		final String sessionId = request.getSessionId();
		final Map<String, String> params = request.getParamsMap();
		Config config = new Config();
		config.setProtocol(request.getProtocol());
		config.setParams(params);
		config.setSessionId(sessionId);
		return config;
	}

	public static Config build(final Request request) {
		Config config = new Config();
		config.setSessionId(request.getSessionId());
		return config;
	}

	public static Config build(final DisconnectRequest request) {
		Config config = new Config();
		config.setSessionId(request.getSessionId());
		return config;
	}

}