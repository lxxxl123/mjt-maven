package com.chen;

import com.dianping.cat.Cat;
import com.dianping.cat.message.ForkedTransaction;
import com.dianping.cat.message.Transaction;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

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
/**
 * @author chenwh
 * @date 2021/4/14
 */
@Slf4j
public class TestFork {

    public static void main(String[] args) throws Exception {
        String neName = "device-25";
        // 1.新建事务并start(forked=true)当前事务并没有压入m_stack  ,
        // 2.初始化事务的messageId, 保存到ForkedTransaction对象中
        ForkedTransaction forkedTransaction = Cat.newForkedTransaction("LOGIN", neName);

        // 执行登录
        CompletableFuture<Void> future = connect(neName);

        Cat.logMetricForCount("LOGIN-NE123",5);
        //异步回调代码
        future.whenCompleteAsync((viid, ex) -> {
            // 1.start(forked=false) 并把事务压入当前线程的m_stack中  ,
            // 2.把forkedTransaction中的messageId 保存到当前线程的messageTree中
            forkedTransaction.fork();
            if (ex != null) {
                forkedTransaction.setStatus("LOGIN_ERROR");
                Cat.logError(ex);
            }else{
                forkedTransaction.setStatus(Transaction.SUCCESS);
            }
            forkedTransaction.complete();
        });

        Thread.sleep(100000000); // 此处 sleep 一会, 就能保证 CAT 异步消息发送
    }

    // 模拟网元登录
    private static CompletableFuture<Void> connect(final String neName) {
        return CompletableFuture.runAsync(() -> {
            System.out.println(String.format("try login %s", neName));
            try {
                System.out.println("休眠3s");
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("login success");
        });
    }


}
