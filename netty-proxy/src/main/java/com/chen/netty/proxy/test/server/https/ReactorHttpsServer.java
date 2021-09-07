package com.chen.netty.proxy.test.server.https;

import com.chen.netty.proxy.test.server.Server;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServer;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;

/**
 * @author chenwh
 * @date 2021/9/7
 */

public class ReactorHttpsServer implements Server {

    @Override
    public void create() throws Exception {
        HttpServer server = HttpServer.create()
                .port(8998)
                .route(r -> {
                    r.get("/hello", (req, res) -> {
                        return res.header(CONTENT_TYPE, TEXT_PLAIN).sendString(Mono.just(
                                "======================= \n Hello World! \n========================\n"));
                    });
                    r.post("/hello", (req, res) -> {
                        return res.send(req.receive().retain());
                    });
                });

        if (true) {
            //随机生成证书
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            server = server.secure(spec -> spec.sslContext(
                    SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey())));
        }

        server.bindNow().onDispose().block();
    }

    public static void main(String[] args) {
        try {
            new ReactorHttpsServer().create();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
