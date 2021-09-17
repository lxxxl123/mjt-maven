package com.chen.rsocket.test.startup;

import io.netty.buffer.Unpooled;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;
@Slf4j
public class CountClient {

    public static void main(String... args) throws Exception {
        RSocket rSocket = RSocketFactory.connect()
                .transport(TcpClientTransport.create(7000))
                .start()
                .block();

        CountDownLatch latch = new CountDownLatch(1);

        rSocket.requestStream(DefaultPayload.create(Unpooled.EMPTY_BUFFER))
                .doOnNext(e -> {
                    log.info("next = {}", e.data().copy().toString(Charset.defaultCharset()));
                })
                .doOnComplete(() -> {
                    log.info("Done");
                    latch.countDown();
                })
                .subscribe(payload -> {
                    byte[] bytes = new byte[payload.data().readableBytes()];
                    payload.data().readBytes(bytes);

                    log.info("Received: {}", new BigInteger(bytes).intValue());
                });

        latch.await();
    }
}
