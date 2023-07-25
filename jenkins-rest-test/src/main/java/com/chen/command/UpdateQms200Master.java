package com.chen.command;

import cn.hutool.core.util.StrUtil;
import com.chen.CopyUtils;
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
    public static final String FEATURE_MASTER_200 = "feature/master_200";

    //        public static final String BRAND_NAME = "feature/qalsData-V1.0.0";

    static GitTool git = new GitTool();

    static {
        git.setMvn(MVM_PATH);
        git.setSh(GIT_PATH);
        git.setCharset("gbk");
    }

    public static void updateBranchAndMergeTo(String path, String frontBranch, String toBranch) {
        git.setPath(path);

        // merge front
        git.exeGit(GIT_COMMIT_AM_TEMP);
        git.exeGit("rm ./.git/index.lock");

        // refresh branch
        git.exeGit("git fetch");

        git.checkout(frontBranch);
        git.exeGit("git fetch");
        git.exeGit("git reset --hard origin/" + frontBranch);

        git.checkout(toBranch);
        git.exeGit("git fetch");
        git.exeGit("git reset --hard origin/" + toBranch);

        git.exeGit("git merge " + frontBranch);

        git.exeGit("git push");

        log.info("\n分支合并完成" + frontBranch + " -> " + toBranch);

    }

    public static void buildFrontAndCopy(String branchName, String frontPath, String backPath, boolean buildNpm) {
        git.setPath(backPath);
        git.checkout(branchName);

        if (buildNpm) {
            git.setPath(frontPath);
            git.setBranch(branchName);
            git.exeGit("git pull");
            git.setCharset("utf-8");
            String res = git.exeMvn("mvn clean install -f pom.xml");
            if (StrUtil.containsAny(res, "Failure", "Command execution failed")) {
                throw new RuntimeException("Build Failure");
            }
        }


        git.setCharset("gbk");

        git.setPath(backPath);
        git.checkout(branchName + "-front-end");

        git.exeGit("git fetch");
        git.exeGit("git reset --hard origin/" + branchName);
        git.exeGit("git clean -f");

        git.moveFile("sh update-front-cp.sh");
        git.exeGit("git add \"qms-service/src/main/resources/static/*\"");
        git.exeGit("git commit -am \"前端代码\"");
        git.exeGit("git push -f");

        git.checkout(branchName);
        log.info("\n前端代码构建完成" + branchName);

    }

    public static void build() throws Exception {
        String frontEndBranch = FEATURE_MASTER_200 + "-front-end";

//        updateBranchAndMergeTo(FRONT_PATH_CP, BRAND_NAME, FEATURE_MASTER_200);
//
//        updateBranchAndMergeTo(BACK_END_PATH_CP, BRAND_NAME, FEATURE_MASTER_200);

//
        buildFrontAndCopy(FEATURE_MASTER_200, FRONT_PATH_CP, BACK_END_PATH_CP, false);
//
        JobManager.buildAndDeployQmsPlatform(frontEndBranch);
    }

    public static void main(String[] args) throws Exception {
        build();
    }
}
