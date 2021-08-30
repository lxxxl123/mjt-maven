package com.chen.netty.proxy.test;

import com.chen.netty.proxy.test.server.http.HttpProxyServer;
import com.chen.netty.proxy.test.server.http.SimHttpServer;

import java.util.concurrent.TimeUnit;

/**
 * @author chenwh
 * @date 2021/8/26
 */

public class Application {


    public static void main(String[] args) throws Exception {
        new SimHttpServer().create();
        new HttpProxyServer().create();
        TimeUnit.DAYS.sleep(2);

    }
}
