package com.chen.command;

import com.chen.CopyUtils;
import com.chen.GitTool;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author chenwh3
 */
public class UpdateHip {


    public static final String projectSvnPath = "D:\\workspaces\\hip-prod";
    public static final String projectGitPath = "D:\\20221014\\HIP";

    public static void copyFile(int rowSize) throws IOException {
        GitTool gitTool = new GitTool();
        gitTool.setPath(projectSvnPath);
        gitTool.exeGit("svn update");
        String oriPath = projectGitPath;
        String aimPath = projectSvnPath;
        CopyUtils.gitCopy(oriPath, aimPath, "git diff --name-status -a head head~" + rowSize);
        Desktop.getDesktop().open(new File(projectSvnPath));
    }


    /**
     * 更新步骤
     * YD任务单-打包
     * 打包项目：质检HIP
     * 查询提交记录
     * 使用清单报告
     * YD任务单-明细
     * copy到800
     * Jenkins更新
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        copyFile(1);
//        buildApi();
    }
}
