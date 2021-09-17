package com.chen.rsocket.test.startup;

import io.rsocket.*;
import io.rsocket.frame.decoder.PayloadDecoder;
import io.rsocket.transport.netty.server.TcpServerTransport;
import io.rsocket.util.DefaultPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

public class CountService {
    private static final Logger LOG = LoggerFactory.getLogger(CountService.class);

    public static void main(String... args) throws Exception {
        RSocketFactory.receive()
                .frameDecoder(PayloadDecoder.DEFAULT)
                .acceptor(new SocketAcceptor() {
                    @Override
                    public Mono<RSocket> accept(ConnectionSetupPayload setup, RSocket sendingSocket) {
                        return Mono.just(new AbstractRSocket() {
                            @Override
                            public Flux<Payload> requestStream(Payload payload) {
                                return Flux.create(payloadFluxSink -> {
                                    for (int i = 1; i <= 100; i++) {
                                        LOG.info("Sending: {}", i);
                                        payloadFluxSink.next(DefaultPayload.create(BigInteger.valueOf(i).toByteArray()));
                                    }

                                    payloadFluxSink.complete();
                                });
                            }
                        });
                    }
                })
                .transport(TcpServerTransport.create(7000))
                .start()
                .block();

        LOG.info("RSocket server started on port: 7000");

        Thread.currentThread().join();
    }
}