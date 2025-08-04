package com.chen.command;

import cn.hutool.core.util.StrUtil;
import com.chen.CopyUtils;
import com.chen.GitObj;
import com.chen.GitTool;
import com.chen.JobManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author chenwh3
 */
public class UpdateQms200 {

    public static final String GIT_COMMIT_AM_TEMP = "git commit -am \"temp\"";

    public static final String MVM_PATH = "D:\\\\code\\\\maven\\\\apache-maven-3.8.6\\\\bin\\\\mvn.cmd";
    public static final String GIT_PATH = "C:\\\\Program Files\\\\Git\\\\bin\\\\sh.exe";
    public static final String FRONT_PATH = "D:\\20221014\\qms-front\\";
    public static final String BACK_END_PATH = "D:\\20221014\\qms-platform\\";

    public static final String BACK_END_PATH_CP = "D:\\20221014\\qms-platform-copy\\";

        public static final String BRAND_NAME = "feature/qalsData-V1.0.0";
//    public static final String BRAND_NAME = "feature/chargReport-v1.0.0";
//    public static final String BRAND_NAME = "feature/QT07-v1.0.0";


    public static void build(boolean buildFront) throws Exception {

        String frontEndName = BRAND_NAME + "-front-end";

        GitObj frontGit = new GitObj(FRONT_PATH);

        /**
         *  build front end , 构建前端
         */
        if (buildFront) {
            String res = frontGit.git.exeMvn("mvn clean install -f pom.xml -Dfile.encoding=utf-8");
            if (StrUtil.containsAny(res, "Failure", "Command execution failed")) {
                throw new RuntimeException("Build Failure");
            }
        }

        GitObj backGit = new GitObj(BACK_END_PATH);

        GitObj backCpGit = new GitObj(BACK_END_PATH_CP, true);
        backCpGit.git.checkout(BRAND_NAME);
        backCpGit.git.exeGit("git reset --hard origin/" + BRAND_NAME);

//        CopyUtils.copyFile(BACK_END_PATH, BACK_END_PATH_CP, "git diff --name-status -a head head~" + 5);

//        backCpGit.git.exeGit(GIT_COMMIT_AM_TEMP);

        //作用不大
//        backCpGit.git.exeGit("git rebase origin/master");
//        backCpGit.git.exeGit("git clean -f -d");
//        backCpGit.git.exeGit("git push --force --set-upstream origin " + BRAND_NAME);

        /*切换分支*/
        backCpGit.git.exeGit("git branch " + frontEndName);
        backCpGit.git.checkout(frontEndName);
        backCpGit.git.exeGit("git reset --hard origin/" + BRAND_NAME);
        backCpGit.git.exeGit("git clean -f -d");
        if (StrUtil.isNotBlank(backMergeBranch)) {
            backCpGit.git.merge( backMergeBranch);
        }
        if (StrUtil.isNotBlank(backMergeBranch1)) {
            backCpGit.git.merge(backMergeBranch1);
        }

        if (StrUtil.isNotBlank(backMergeBranch2)) {
            backCpGit.git.merge(backMergeBranch2);
        }

        if (StrUtil.isNotBlank(backMergeBranch3)) {
            backCpGit.git.merge(backMergeBranch3);
        }
        backCpGit.git.moveFile("sh update-front.sh");
        backCpGit.git.exeGit("git add \"qms-service/src/main/resources/static/*\"");
        backCpGit.git.exeGit(GIT_COMMIT_AM_TEMP);
        backCpGit.git.exeGit("git push --force --set-upstream origin " + frontEndName);

        JobManager.buildAndDeployQmsPlatform(frontEndName);
    }

    private static String backMergeBranch1 = "";
//    private static String backMergeBranch1 = "origin/feature/dongbj-dev-V2.0.0";

    private static String backMergeBranch = "";
//    private static String backMergeBranch = "origin/feature/qualityFollow-V1.0.0";
    private static String backMergeBranch2 = "";
//    private static String backMergeBranch2 = "origin/feature/djw-dev2.0.0";

        private static String backMergeBranch3 = "";
//    private static String backMergeBranch3 = "origin/feature/yuxj-dev1.0";

    public static void main(String[] args) throws Exception {
        build(true);
//        build(false);
//        JobManager.buildAndDeployQmsPlatform(BRAND_NAME + "-front-end");
    }

}
