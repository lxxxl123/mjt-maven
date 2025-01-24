package com.chen.command;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {

    static AtomicInteger sum = new AtomicInteger(0);


    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        List<Thread> threads = new ArrayList<>();
//        List<CompletableFuture> threads = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            Thread thread = new Thread(() -> sum.incrementAndGet());
            thread.start();
            threads.add(thread);
//            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> sum.incrementAndGet());
//            threads.add(future);
        }

        for (Thread thread : threads) {
            thread.join();
        }

//        for (CompletableFuture thread : threads) {
//            thread.join();
//        }

        System.out.println("sum:" + sum.get());
        System.out.println("time:" + (System.currentTimeMillis() - start));
        TimeUnit.SECONDS.sleep(50);
    }
}
