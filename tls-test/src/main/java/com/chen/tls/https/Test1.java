package com.chen.tls.https;

import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServer;

import java.io.InputStream;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;

/**
 * @author chenwh
 * @date 2021/9/7
 */

public class Test1 implements Server {

    @Override
    public void create() throws Exception {
        HttpServer server = HttpServer.create()
                .port(8997)
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

        if (true) {
//            noJks(server);
            server = perfJks(server);
        }

        server.bindNow().onDispose().block();
    }


    public static HttpServer perfJks(HttpServer server) throws Exception{
        InputStream pem1 = ClassLoader.getSystemResourceAsStream("./temp.pem");
        InputStream pem2 = ClassLoader.getSystemResourceAsStream("./temp.pem");

        /**
         * 指定证书和私钥
         */
        server = server.secure(spec -> spec.sslContext(
                SslContextBuilder.forServer(pem1, pem2))
        );
        return server;
    }

    public static HttpServer noJks(HttpServer server) throws Exception{
        //随机生成证书
        SelfSignedCertificate ssc = new SelfSignedCertificate();
        server = server.secure(spec -> spec.sslContext(
                SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()))
        );
        return server;
    }

    public static void main(String[] args) {
        try {
            new Test1().create();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
