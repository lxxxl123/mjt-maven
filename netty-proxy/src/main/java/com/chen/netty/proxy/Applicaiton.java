package com.chen.netty.proxy;

import com.chen.netty.proxy.model.ServerConfig;
import com.chen.netty.proxy.rsocket.ConnectionListener;
import com.chen.netty.proxy.server.RpcServer;
import com.chen.netty.proxy.server.TransmitServer;
import com.chen.netty.proxy.transmit.service.HttpProxyService;
import com.chen.netty.proxy.transmit.service.HttpProxyServiceImpl;
import com.chen.netty.proxy.transmit.service.TransportService;
import com.chen.netty.proxy.transmit.service.TransportServiceImpl;
import io.rsocket.DuplexConnection;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chenwh
 * @date 2021/8/25
 */
@Slf4j
public class Applicaiton {


    private void startRpcServer(ServerConfig config) throws Exception {
        RpcServer.create().port(config.getPort())
                .addConnectionListener(new ConnectionListener() {
                    @Override
                    public void connectionClosed(DuplexConnection connection) {
                        final String address = connection.remoteAddress().toString();
                        log.info("客户端[{}]断开连接.", address);
                    }
                }).addService(TransportService.class, new TransportServiceImpl())
                // http代理转发
                .addService(HttpProxyService.class, new HttpProxyServiceImpl()).start();
    }
}
