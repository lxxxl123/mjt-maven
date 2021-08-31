package com.chen.netty.proxy.test;

import com.chen.netty.proxy.test.server.http.HttpProxyServer;
import com.chen.netty.proxy.test.server.http.SimHttpServer;

import java.util.concurrent.TimeUnit;

/**
 * 问题 : 高并发下:200*10/1s
 *      部分连接不关闭
 *      部分response不完整
 * 解决方法: 增大readidle time后消失
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
