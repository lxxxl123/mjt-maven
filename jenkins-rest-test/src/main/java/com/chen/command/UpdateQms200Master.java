package com.chen.command;

import cn.hutool.core.util.StrUtil;
import com.chen.GitObj;
import com.chen.GitTool;
import com.chen.JobManager;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chenwh3
 */
@Slf4j
public class UpdateQms200Master {

    public static final String GIT_COMMIT_AM_TEMP = "git commit -am \"temp\"";

    public static final String BRAND_NAME = "feature/chargReport-v1.0.0";
    public static final String MVM_PATH = "D:\\\\code\\\\maven\\\\apache-maven-3.8.6\\\\bin\\\\mvn.cmd";
    public static final String GIT_PATH = "C:\\\\Program Files\\\\Git\\\\bin\\\\sh.exe";
    public static final String FRONT_PATH_CP = "D:\\workspace-new\\qms-front-copy\\";
    public static final String BACK_END_PATH = "D:\\20221014\\qms-platform\\";

    public static final String BACK_END_PATH_CP = "D:\\20221014\\qms-platform-copy\\";

    static GitTool git = new GitTool();

    static {
        git.setMvn(MVM_PATH);
        git.setSh(GIT_PATH);
        git.setCharset("UTF-8");
    }


    public static void buildFrontAndCopy(String branchName, String frontPath, String backPath, boolean buildNpm) {
        log.info("--------------------------------后端代码 reset ");
        GitObj back = new GitObj(backPath, true);
        back.checkoutResetHardPush(branchName, branchName + "(full)");


        log.info("--------------------------------前端切换分支");
        if (buildNpm) {
            GitObj front = new GitObj(frontPath, true);
            front.checkOut(branchName);
            String res = front.git.exeMvn("mvn clean install -f pom.xml");
            if (StrUtil.containsAny(res, "Failure", "Command execution failed")) {
                throw new RuntimeException("Build Failure");
            }
        }


        log.info("--------------------------------后端分支reset2");
        back.checkoutResetHardPush(branchName + "-front-end", branchName);
        back.git.exeGit("git clean -f");

        log.info("--------------------------------前端代码复制");
        back.git.moveFile("sh update-front-cp.sh");
        back.git.exeGit("git add \"qms-service/src/main/resources/static/*\"");
        back.git.exeGit("git commit -am \"前端代码\"");
        back.git.exeGit("git push -f");

//        back.checkOut(branchName+ "(full)");
        log.info("--------------------------------前端代码构建完成 -- {}", branchName);

    }



    public static final String BRAND_QALS_DATA = "feature/qalsData-V1.0.0";
    //feature/master_200(full)
    public static final String BRAND_MASTER_200_FULL = "feature/master_200(full)";

    public static final String BRAND_MASTER_200 = "feature/master_200";


    public static void resetHard(String branch){
        log.info("--------------------------------合并分支 {}", branch);
        GitObj backObj = new GitObj(BACK_END_PATH_CP, true);
        backObj.checkoutResetHardPush(BRAND_MASTER_200_FULL, branch);

        GitObj frontObj = new GitObj(FRONT_PATH_CP, true);
        frontObj.checkoutResetHardPush(BRAND_MASTER_200, branch);
    }

    public static void build() throws Exception {
        String frontEndBranch = BRAND_MASTER_200 + "-front-end";
        JobManager.buildAndDeployQmsPlatform(frontEndBranch);
    }


    public static void main(String[] args) throws Exception {
        resetHard(BRAND_QALS_DATA);
        buildFrontAndCopy(BRAND_MASTER_200, FRONT_PATH_CP, BACK_END_PATH_CP, true);
        build();
    }
}
