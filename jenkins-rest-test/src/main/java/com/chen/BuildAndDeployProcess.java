package com.chen;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author chenwh3
 */
@Slf4j
public abstract class BuildAndDeployProcess {
    protected Session session = new Session();

    protected int wait = 3;

    public void buildAndDeploy(String buildJobName, String developJobName) throws IOException, InterruptedException {
        StopWatch sw = StopWatch.createStarted();
        log.info("开始登录");
        session.login(session.getHost() + "/j_spring_security_check", "dev", "dev0407@");
        log.info("开始构建");
        String job = null;
        if (buildJobName != null) {
            job = buildJobName;
            build(job);
            TimeUnit.SECONDS.sleep(wait);
            while (session.getProcess(job)) {
                log.info("构建中");
                TimeUnit.SECONDS.sleep(wait);
            }
            System.out.println(session.getConsole(job));
        }
        log.info("开始部署");
        job = developJobName;
        deploy(job);
        TimeUnit.SECONDS.sleep(wait);
        while (session.getProcess(job)) {
            log.info("部署中");
            TimeUnit.SECONDS.sleep(wait);
        }
        System.out.println(session.getConsole(job));
        sw.stop();
        log.info("部署完成 耗时 = {}", sw);
    }

    public abstract void build(String jobName) throws IOException;
    public abstract void deploy(String jobName) throws IOException;

    public static void main(String[] args) throws Exception {
        String buildJobName = "qms-platform-build";

        Session session = new Session();
        int wait = 3;
        StopWatch sw = StopWatch.createStarted();
        log.info("开始登录");
        session.login(session.getHost() + "/j_spring_security_check", "dev", "dev0407@");
        log.info("开始构建");
        String job = null;
        if (buildJobName != null) {
            job = buildJobName;
            session.build(job, "feature/qalsData-V1.0.0-front-end");
            TimeUnit.SECONDS.sleep(wait);
            while (session.getProcess(job)) {
                log.info("构建中");
                TimeUnit.SECONDS.sleep(wait);
            }
            System.out.println(session.getConsole(job));
        }
    }

}
