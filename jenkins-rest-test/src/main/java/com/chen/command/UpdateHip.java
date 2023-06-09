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
public class UpdateHip {


    public static final String projectSvnPath = "D:\\test\\HIP";
    public static final String projectGitPath = "D:\\20221014\\HIP";

    public static void copyFile(int rowSize) throws IOException {
        GitTool gitTool = new GitTool();
        gitTool.setPath(projectSvnPath);
        gitTool.exeGit("svn update");
        String oriPath = projectGitPath;
        String aimPath = projectSvnPath;
        CopyUtils.copyFile(oriPath, aimPath, "git diff --name-status -a head head~" + rowSize);
        Desktop.getDesktop().open(new File(projectSvnPath));
    }



    public static void main(String[] args) throws Exception {
        copyFile(1);
//        buildApi();
    }
}
