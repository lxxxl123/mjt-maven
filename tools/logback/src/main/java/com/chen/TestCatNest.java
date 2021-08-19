package com.chen;

import com.dianping.cat.Cat;
import com.dianping.cat.message.ForkedTransaction;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import io.netty.util.concurrent.ImmediateEventExecutor;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import javax.print.attribute.standard.MediaSize;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author chenwh
 * @date 2021/4/14
 */
@Slf4j
public class TestCatNest {


    public static void main(String[] args) throws Exception {
        Transaction t = Cat.newTransaction("Nest", "1");

        Transaction t1 = Cat.newTransaction("Nest", "2");



        System.out.println(Cat.getCurrentMessageId());
        TimeUnit.SECONDS.sleep(1);
        Cat.logMetricForCount("temp-metric", 1);

        t1.setStatus(Message.SUCCESS);

        t.setStatus(Message.SUCCESS);

        t1.complete();
        t.complete();
        TimeUnit.SECONDS.sleep(1000);

    }



}
