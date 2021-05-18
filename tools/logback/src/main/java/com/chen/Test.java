package com.chen;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.ForkedTransaction;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import io.netty.util.concurrent.ImmediateEventExecutor;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chenwh
 * @date 2021/4/14
 */
@Slf4j
public class Test {
    public static void main(String[] args) throws Exception {
        String dataSourceName = "device-25";
        Transaction t = Cat.newTransaction("LOGIN-ROOT", "ROOT");
        ForkedTransaction forkedTransaction = Cat.newForkedTransaction("LOGIN",
                dataSourceName);

        Cat.logMetricForCount(String.format("login-%s", dataSourceName));
        Cat.logMetricForDuration(String.format("login-%s",dataSourceName),1);
        // 执行登录
        Promise<String> promise = connect(dataSourceName);
        promise.addListener(future -> {
            forkedTransaction.fork();
            if (future.get() == "SUCCESS") {
                forkedTransaction.setStatus(Transaction.SUCCESS);
            }else{
                Cat.logError(String.format("login Error neName=%s",dataSourceName), new RuntimeException("login error"));
                Cat.logMetricForDuration(String.format("login-failed-%s",dataSourceName),1);
                forkedTransaction.setStatus("LOGIN ERROR");
            }
            forkedTransaction.complete();
        });

        new Thread(() -> {
            try {
                System.out.println("休眠3s");
                Thread.sleep(2000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            // promise.setFailure(new IllegalStateException("Connect error"));
            promise.setSuccess(null);
        }).start();

        t.setStatus(Message.SUCCESS);
        t.complete();
        Thread.currentThread().join();
        Thread.sleep(100000000); // 此处 sleep 一会, 就能保证 CAT 异步消息发送
    }

    // 模拟网元登录
    private static Promise<String> connect(final String dataSourceName) {
        System.out.println(
                "Thread:" + Thread.currentThread().getName() + " 执行登录:" + dataSourceName);
        return ImmediateEventExecutor.INSTANCE.newPromise();
    }


}
