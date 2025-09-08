package com.chen.command;

import com.chen.GitObj;
import com.chen.JobManager;

/**
 * @author chenwh3
 */
public class UpdateApi200 {


    public static final String BACK_END_PATH = "D:\\20221014\\idea-workspace\\qmsapicenter";

    public static final String BRAND_NAME_Develop = "develop";
    public static final String BRAND_NAME_Master = "master";
    public static final String BRAND_NAME_Chenwh3 = "feature/chenwh3";


    public static void build(String brandName) throws Exception {



        GitObj backCpGit = new GitObj(BACK_END_PATH, true);
//        backCpGit.git.checkout(brandName);
//        backCpGit.git.exeGit("git reset --hard origin/" + brandName);

//        CopyUtils.copyFile(BACK_END_PATH, BACK_END_PATH_CP, "git diff --name-status -a head head~" + 5);

//        backCpGit.git.exeGit(GIT_COMMIT_AM_TEMP);

        //作用不大
//        backCpGit.git.exeGit("git rebase origin/master");
//        backCpGit.git.exeGit("git clean -f -d");
//        backCpGit.git.exeGit("git push --force --set-upstream origin " + BRAND_NAME);

        /*切换分支*/
//        backCpGit.git.exeGit("git branch " + brandName);
//        backCpGit.git.checkout(brandName);

        JobManager.buildApi(brandName);
    }

    public static void main(String[] args) throws Exception {
//        build(BRAND_NAME_Master);
        build(BRAND_NAME_Develop);
//        build(BRAND_NAME_Chenwh3);
    }

}
