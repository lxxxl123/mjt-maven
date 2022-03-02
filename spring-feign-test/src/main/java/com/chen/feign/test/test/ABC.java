package com.chen.feign.test.test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author chenwh
 * @date 2022/2/23
 */

public class ABC {

    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock();
        reentrantLock.newCondition();
        String s = "";
        String k = "";

        CompletableFuture.runAsync(() -> {
            synchronized (s) {
                System.out.println("thread-1");
                try {
                    k.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("thread-1-finish");
            }
        });

        CompletableFuture.runAsync(() -> {
            synchronized (s) {
                System.out.println("thread-2");
                k.notify();
                System.out.println("thread-2-finish");
            }
        });
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
