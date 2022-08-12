package com.chen;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author chenwh3
 */
@Slf4j
public class JobManager {

    public static void buildAndDeployQmsPlatform(String branchName) throws IOException, InterruptedException {
        new BuildAndDeployProcess() {
            @Override
            public void build(String jobName) throws IOException {
                log.info("开始部署分支: {}", branchName);
                session.build(jobName, branchName);
            }

            @Override
            public void deploy(String jobName) throws IOException {
                session.triggerJob(jobName);
            }
        }.buildAndDeploy("qms-platform-build", "qms-platform-deploy");
    }

    public static void buildTrigger(String buildName,String jobName) throws IOException, InterruptedException {
        new BuildAndDeployProcess() {
            @Override
            public void build(String jobName) throws IOException {
                session.triggerJob(jobName);
            }

            @Override
            public void deploy(String jobName) throws IOException {
                session.triggerJob(jobName);
            }
        }.buildAndDeploy(buildName, jobName);

    }

    public static void buildSync() throws IOException, InterruptedException {
        buildTrigger("QmsApiCenter_build_develop", "QmsApiCenter_deploy");
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        String branchName = "feature/market-complainV1.0.0-front-end";
//        buildAndDeployQmsPlatform(branchName);
        buildSync();
//        buildTrigger("1", "1");
    }
}
