package com.chen.tls.https;

import io.netty.handler.ssl.SslContextBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.server.HttpServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;
/**

 * @date 2022/1/17
 */

public class TestHttpsServer {


    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer
                .create()
                .protocol(HttpProtocol.HTTP11)
                .port(8988)
                .route(r -> {
                    r.get("/hello", (req, res) -> {
                        return res.header(CONTENT_TYPE, TEXT_PLAIN)
                                .sendString(Mono.just(
                                        "======================= \n Hello World! \n========================\n"));
                    });
                    r.post("/hello", (req, res) -> {
                        return res.send(req.receive().retain());
                    });
                });


        InputStream cert = ClassLoader.getSystemResourceAsStream("./cert.pem");
        InputStream key = ClassLoader.getSystemResourceAsStream("./private.pem");

        server = server.secure(ssl -> ssl.sslContext(SslContextBuilder
                .forServer(cert, key)));

        cert.close();
        key.close();

        server.bindNow()
                .onDispose().block();
    }


}
