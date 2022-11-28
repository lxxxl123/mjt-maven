package com.chen.command;

import com.chen.CopyUtils;
import com.chen.JobManager;

import java.io.IOException;

/**
 * @author chenwh3
 */
public class UpdateApi200 {

    public static void copyFile(){
        String oriPath = "D:\\20221014\\idea-workspace\\qmsapicenter";
        String aimPath = "D:\\20221014\\qmsApicenter\\qmsApiCenter";
        CopyUtils.copyFile(oriPath, aimPath, "git diff --name-only head head~1");
    }

    public static void buildApi() throws IOException, InterruptedException {
        JobManager.buildTrigger("QmsApiCenter_build_develop", "QmsApiCenter_deploy");
    }


    public static void main(String[] args) throws Exception {
//        copyFile();
//        buildApi();
    }
}
