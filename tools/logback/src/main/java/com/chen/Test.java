package com.chen;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.Transaction;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chenwh
 * @date 2021/4/14
 */
@Slf4j
public class Test {
    public static void main(String[] args) throws Exception {
        CatContextImpl catContext = new CatContextImpl();
        catContext.addProperty(Cat.Context.ROOT, "123");
        catContext.addProperty(Cat.Context.PARENT, "234");
        catContext.addProperty(Cat.Context.CHILD, "345");
        Cat.logRemoteCallServer(catContext);
        Transaction t = Cat.newTransaction("safsfasd", "demo");
        Cat.logMetric("fuck");
        Cat.logMetricForCount("fuck");
        Cat.logMetricForCount("fuck");
        Cat.logMetricForCount("fuck", 5);

        Cat.logEvent("dddd", "ccccc", Event.SUCCESS, "ip=${serverIp}");
        t.complete();
        Cat.logEvent("aaaaaaa", "hfkjahsfjkahds", Event.SUCCESS, "ip=${serverIp}");
        System.out.println("end");

        Thread.sleep(100000000); // 此处 sleep 一会, 就能保证 CAT 异步消息发送
    }
}
