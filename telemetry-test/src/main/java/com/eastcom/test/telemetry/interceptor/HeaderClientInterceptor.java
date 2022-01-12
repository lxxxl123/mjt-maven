package com.eastcom.test.telemetry.interceptor;

import io.grpc.*;
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall;
import io.grpc.ForwardingClientCallListener.SimpleForwardingClientCallListener;

import java.util.HashMap;
import java.util.Map;

public class HeaderClientInterceptor implements ClientInterceptor {

    private Map<String, String> map = new HashMap<>();

    public HeaderClientInterceptor setHeader(String key, String val) {
        map.put(key, val);
        return this;
    }


    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method,
                                                               CallOptions callOptions, Channel next) {
        return new SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                /* put custom header */
                map.forEach((k, v) -> headers.put(Metadata.Key.of(k, Metadata.ASCII_STRING_MARSHALLER), v));
                super.start(new SimpleForwardingClientCallListener<RespT>(responseListener) {
                    @Override
                    public void onHeaders(Metadata headers) {
                        /**
                         * if you don't need receive header from server,
                         * you can use {@link io.grpc.stub.MetadataUtils#attachHeaders}
                         * directly to send header
                         */
                        super.onHeaders(headers);
                    }
                }, headers);
            }
        };
    }
}