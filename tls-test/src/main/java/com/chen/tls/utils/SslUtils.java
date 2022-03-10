package com.chen.tls.utils;

import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import javax.net.ssl.SSLEngine;
import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author chenwh
 * @date 2022/1/18
 */

public class SslUtils {

    public static File loadClassFile(String path) throws Exception {
        return new File(ClassLoader.getSystemResource(path).toURI());
    }

    public static SSLEngine serverSslEngine(String cert, String key, ByteBufAllocator byteBufAllocator) throws Exception {
        return SslContextBuilder
                .forServer(loadClassFile(cert), loadClassFile(key))
                .build()
                .newEngine(byteBufAllocator);
    }

    public static SSLEngine clientSslEngine(String cert,String key,ByteBufAllocator byteBufAllocator) throws Exception {
        return SslContextBuilder.forClient().sslProvider(SslProvider.JDK)
                .keyManager(loadClassFile(cert), loadClassFile(key))
                .trustManager(InsecureTrustManagerFactory.INSTANCE).build()
                .newEngine(byteBufAllocator);
    }


    public static void main(String[] args) {
        Map<String, String> map = new ConcurrentHashMap<>();
        System.out.println(map);

    }

}
