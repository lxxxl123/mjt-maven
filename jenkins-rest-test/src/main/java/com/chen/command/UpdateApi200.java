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


    public static final String projectSvnPath = "D:\\20221014\\qmsApicenter\\qmsApiCenter";
    public static final String projectGitPath = "D:\\20221014\\idea-workspace\\qmsapicenter";

    public static void copyFile(int rowSize) throws IOException {
        GitTool gitTool = new GitTool();
        gitTool.setPath(projectSvnPath);
        gitTool.exeGit("svn update");
        String oriPath = projectGitPath;
        String aimPath = projectSvnPath;
        CopyUtils.gitCopy(oriPath, aimPath, "git diff --name-status -a head head~" + rowSize);
        Desktop.getDesktop().open(new File(projectSvnPath));
    }

    public static void buildApi() throws IOException, InterruptedException {
        JobManager.buildTrigger("QmsApiCenter_build_develop", "QmsApiCenter_deploy");
    }


    public static void main(String[] args) throws Exception {
        copyFile(10);
//        buildApi();


    }
}
