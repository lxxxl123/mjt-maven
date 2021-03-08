package com.chen;

import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;

import java.net.URI;

/**
 * @author chenwh
 * @date 2021/3/5
 */

public class Client {
    public static void main(String[] args) throws Exception{
        WebSocketClient client = new ReactorNettyWebSocketClient();
        URI url = new URI("ws://localhost:8080/path");
        client.execute(url, session ->
                session.receive()
                        .doOnNext(System.out::println)
                        .then());
    }
}
