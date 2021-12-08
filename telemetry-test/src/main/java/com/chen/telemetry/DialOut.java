package com.chen.telemetry;

import io.grpc.ServerBuilder;

/**
 * @author chenwh
 * @date 2021/12/3
 */

public class DialOut {


    public static void main(String[] args) {
        ServerBuilder.forPort(50050)
                .build();
    }
}
