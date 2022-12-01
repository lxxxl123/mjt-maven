package com.chen.command;

import com.chen.CopyUtils;
import com.chen.GitTool;
import com.chen.JobManager;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author chenwh3
 */
public class UpdateApi200 {

    public static void copyFile() throws IOException {
        GitTool gitTool = new GitTool();
        gitTool.setPath("D:\\20221014\\qmsApicenter\\qmsApiCenter");
        gitTool.exeGit("svn update");
        String oriPath = "D:\\20221014\\idea-workspace\\qmsapicenter";
        String aimPath = "D:\\20221014\\qmsApicenter\\qmsApiCenter";
        CopyUtils.copyFile(oriPath, aimPath, "git diff --name-only head head~1");
        Desktop.getDesktop().open(new File("D:\\20221014\\qmsApicenter\\qmsApiCenter"));
    }

    public static void buildApi() throws IOException, InterruptedException {
        JobManager.buildTrigger("QmsApiCenter_build_develop", "QmsApiCenter_deploy");
    }


    public static void main(String[] args) throws Exception {
        copyFile();
//        buildApi();
    }
}
