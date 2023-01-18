package com.chen.command;

import com.chen.GitTool;
import com.chen.JobManager;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author chenwh3
 */
public class UpdateQms200 {

    public static final String GIT_COMMIT_AM_TEMP = "git commit -am \"temp\"";

    //    public static final String BRAND_NAME = "feature/market-complainV1.0.0";
    public static final String BRAND_NAME = "feature/chargReport-v1.0.0";
//    public static final String BRAND_NAME = "feature/sampleCheckIn-v1.0.0";

//        public static final String BRAND_NAME = "feature/qalsData-V1.0.0";

    public static void build(boolean buidlFront) throws Exception {

        GitTool git = new GitTool();
        git.setCharset("gbk");
        String frontEndName = BRAND_NAME + "-front-end";
        git.setMvn("D:\\\\code\\\\maven\\\\apache-maven-3.8.6\\\\bin\\\\mvn.cmd");
        git.setSh("C:\\\\Program Files\\\\Git\\\\bin\\\\sh.exe");
        git.setPath("D:\\20221014\\qms-front\\");
        /**
         *  build front end , 构建前端
         */
        if (buidlFront) {
            git.exeMvn("mvn clean install -f pom.xml");
        }

        // 处理后端数据
        git.setPath("D:\\20221014\\qms-platform\\");
        git.exeGit("rm ./.git/index.lock");
        git.exeGit(GIT_COMMIT_AM_TEMP);
        git.checkout(BRAND_NAME);
        git.exeGit(GIT_COMMIT_AM_TEMP);
        git.exeGit("git rebase origin/master");
        git.exeGit("git branch " + frontEndName);
        git.checkout(frontEndName);
        git.exeGit("git reset --hard " + BRAND_NAME);
        git.moveFile("sh update-front.sh");
        git.exeGit("git add \"qms-service/src/main/resources/static/*\"");
        git.exeGit(GIT_COMMIT_AM_TEMP);
        git.exeGit("git push --force");
        git.exeGit("git checkout -f " + BRAND_NAME);
        JobManager.buildAndDeployQmsPlatform(frontEndName);
    }

    public static void main(String[] args) throws Exception{
        build(true);
//        build(false);
    }
}
