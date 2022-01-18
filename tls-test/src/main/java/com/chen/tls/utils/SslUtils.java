package com.chen.tls.utils;

import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.ssl.SslContextBuilder;

import javax.net.ssl.SSLEngine;
import java.io.File;

/**
 * @author chenwh
 * @date 2022/1/18
 */

public class SslUtils {

    public static File loadClassFile(String path) throws Exception {
        return new File(ClassLoader.getSystemResource(path).toURI());
    }

    public static SSLEngine sslEngine(String cert, String key, ByteBufAllocator byteBufAllocator) throws Exception {
        return SslContextBuilder
                .forServer(loadClassFile(cert), loadClassFile(key))
                .build()
                .newEngine(byteBufAllocator);
    }

}
