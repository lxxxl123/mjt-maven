package com.chen.command;

import cn.hutool.core.util.StrUtil;
import com.chen.CopyUtils;
import com.chen.GitTool;

/**
 * @author chenwh3
 */
public class UpdateLoc {

    public static final String GIT_COMMIT_AM_TEMP = "git commit -am \"temp\"";

    public static final String BRAND_NAME = "feature/chargReport-v1.0.0";
    public static final String MVM_PATH = "D:\\\\code\\\\maven\\\\apache-maven-3.8.6\\\\bin\\\\mvn.cmd";
    public static final String GIT_PATH = "C:\\\\Program Files\\\\Git\\\\bin\\\\sh.exe";
    public static final String FRONT_PATH = "D:\\20221014\\qms-front\\";
    public static final String BACK_END_PATH = "D:\\20221014\\qms-platform\\";

    public static final String BACK_END_PATH_CP = "D:\\20221014\\qms-platform-copy\\";
    //        public static final String BRAND_NAME = "feature/qalsData-V1.0.0";

    public static void build(boolean buidlFront) throws Exception {

        GitTool git = new GitTool();
        git.setCharset("utf-8");
        String frontEndName = BRAND_NAME + "-front-end";
        git.setMvn(MVM_PATH);
        git.setSh(GIT_PATH);
        git.setPath(FRONT_PATH);
        /**
         *  build front end , 构建前端
         */
        if (buidlFront) {
            String res = git.exeMvn("mvn clean install -f pom.xml");
            if (StrUtil.containsAny(res,"Failure","Command execution failed")) {
                throw new RuntimeException("Build Failure");
            }
        }

//        // 处理后端数据
        git.setPath(BACK_END_PATH);
        git.exeGit("git fetch");
        git.exeGit(GIT_COMMIT_AM_TEMP);

        git.setPath(BACK_END_PATH_CP);
        git.exeGit("rm ./.git/index.lock");
        git.checkout(BRAND_NAME);
        git.exeGit("git reset --hard origin/" + BRAND_NAME);

        CopyUtils.gitCopy(BACK_END_PATH, BACK_END_PATH_CP, "git diff --name-status -a head head~" + 5);

        git.exeGit(GIT_COMMIT_AM_TEMP);
        git.exeGit("git rebase origin/master");
        git.exeGit("git clean -f");
        git.exeGit("git branch " + frontEndName);
        git.checkout(frontEndName);
        git.exeGit("git reset --hard origin/" + BRAND_NAME);
        git.exeGit("git clean -f");
        git.moveFile("sh update-front.sh");
        git.exeGit("git add \"qms-service/src/main/resources/static/*\"");
        git.exeGit(GIT_COMMIT_AM_TEMP);
        git.exeGit("git push --force");

        git.exeMvn("clean compile -f pom.xml");
    }

    public static void main(String[] args) throws Exception{
//        build(false);
        build(true);
    }
}
