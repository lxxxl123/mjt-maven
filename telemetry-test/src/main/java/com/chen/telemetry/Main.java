package com.chen.telemetry;


import com.google.protobuf.RepeatedFieldBuilder;
import grpc_service.GrpcServiceGrpc;
import grpc_service.GrpcServiceOuterClass;
import grpc_service.GrpcServiceOuterClass.*;
import io.grpc.Channel;
import io.grpc.ClientInterceptors;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;


/**
 * @author chenwh
 * @date 2021/12/1
 */
@Slf4j
public class Main {

    public static LoginReply login(GrpcServiceGrpc.GrpcServiceBlockingStub client, String username, String password) {
        return client.login(LoginRequest.newBuilder()
                .setUserName(username)
                .setPassword(password)
                .build());
    }

    public static void subscirbe(GrpcServiceGrpc.GrpcServiceBlockingStub client){
        SubscribeReply subscribeReply = client.subscribeByStreamName(SubscribeRequest.newBuilder()
                .setStreamName("??").build());
        System.out.println(subscribeReply.getResult());

    }

    public static void main(String[] args) {
        Channel channel = ManagedChannelBuilder.forTarget("192.168.99.254:50051")
                .usePlaintext()
                .build();
        GrpcServiceGrpc.GrpcServiceBlockingStub client = GrpcServiceGrpc.newBlockingStub(channel);

        LoginReply reply = login(client, "ipnet", "admin");
        String tokenId = reply.getTokenId();
        log.info("get tokenId = {}", tokenId);
        HeaderClientInterceptor metadata = new HeaderClientInterceptor();
        metadata.setHeader("token_id", tokenId+1234);
        channel = ClientInterceptors.intercept(channel, metadata);
        client = GrpcServiceGrpc.newBlockingStub(channel);



        SubscribeReply sub1 = client.subscribeByStreamName(SubscribeRequest.newBuilder().setStreamName("sub1").build());

    }
}
