package com.chen.netty.proxy.test.server.https;

import com.chen.netty.proxy.test.server.Server;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

import java.net.InetSocketAddress;
import java.time.Duration;

/**
 * @author chenwh
 * @date 2021/9/7
 */

public class ReactorHttpClient implements Server {


    @Override
    public void create() throws Exception {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(20))
                .proxy(proxy -> proxy.type(ProxyProvider.Proxy.HTTP).host("127.0.0.1")
                        .port(9943).username("admin").password(s -> "1234")

                );

        if (true) {
            httpClient = httpClient.secure(spec -> spec.sslContext(SslContextBuilder
                    .forClient().trustManager(InsecureTrustManagerFactory.INSTANCE)));
        }
        // Netty enforce HTTP proxy to support HTTP CONNECT method
        // https://github.com/netty/netty/issues/10475

        String result = httpClient
                .remoteAddress(() -> new InetSocketAddress("127.0.0.1", 8210)).get()
                .uri("/hello").responseContent().aggregate().asString().block();
        System.out.println(result);
    }

    public static void main(String[] args) {
        try {
            new ReactorHttpClient().create();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
