package com.chen.telemetry;

import grpc_dialout.GrpcDialout;
import grpc_dialout.ReadGrpc;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

/**
 * @author chenwh
 * @date 2021/12/3
 */

public class DialOut {


    public static void main(String[] args) {
        ServerBuilder.forPort(50050)
                .addService(new GrpcDialoutImpl())
                .build();
    }
}
