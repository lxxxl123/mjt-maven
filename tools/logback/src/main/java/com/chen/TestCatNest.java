package com.chen;

import com.dianping.cat.Cat;
import com.dianping.cat.message.ForkedTransaction;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import io.netty.util.concurrent.ImmediateEventExecutor;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

/**
 * @author chenwh
 * @date 2021/4/14
 */
@Slf4j
public class TestCatNest {

//    private void connect(Request request){
//
//        //获取网管名(OMC)
//        String managerName = request.getManagerName() == null ? "direct" : request.getManagerName();
//        //获取应用名
//        String app = request.getApp() == null ? "other" : request.getApp();
//        //创建父线程事务
//        Transaction t = Cat.newTransaction("LOGIN_MML_FROM", app);
//        //为MDC设置traceid
//        CatServiceLogUtils.initTraceId();
//        try {
//            //创建子线程事务,name为网元名
//            ForkedTransaction forkedTransaction = Cat.newForkedTransaction("LOGIN_MML_TO", managerName );
//            //统计网管登录次数(OMC)
//            Cat.logMetricForCount(String.format("login-manager:%s", managerName));
//            //统计应用登录次数(app)
//            Cat.logMetricForCount(String.format("login-app:%s", app));
//            // 执行登录
//            log.info("开始登录");
//            Promise<Response> promise = connect(request);
//            // 异步获取登录结果
//            promise.addListener(future -> {
//                //根据子线程事务 , 创建子线程的消息树
//                forkedTransaction.fork();
//                //获取结果
//                Response res = future.get();
//                if (res.getCode() == SUCCESS) {
//                    forkedTransaction.setStatus(Transaction.SUCCESS);
//                } else {
//                    //统计网管登录失败次数(OMC)
//                    Cat.logMetricForCount(String.format("login-failed-manager:%s", managerName));
//                    //统计应用登录失败次数
//                    Cat.logMetricForCount(String.format("login-failed-app:%s", app));
//                    //记录登录网元错误信息
//                    Cat.logError(String.format("login_error:neName=%s", neName), new DispatchException(res.getMsg()));
//                    forkedTransaction.setStatus("LOGIN_ERROR");
//                }
//                forkedTransaction.complete();
//            });
//        }finally {
//            CatServiceLogUtils.clearTraceId();
//            t.setStatus(Message.SUCCESS);
//            t.complete();
//        }
//    }

    public static void main(String[] args) throws Exception {
        Transaction t = Cat.newTransaction("Nest", "1");

        System.out.println("简单校验");

        t.setStatus(Message.SUCCESS);

    }

    // 模拟网元登录
    private static Promise<String> connect(final String neName) {
        System.out.println(
                "Thread:" + Thread.currentThread().getName() + " 执行登录:" + neName);
        return ImmediateEventExecutor.INSTANCE.newPromise();
    }


}
