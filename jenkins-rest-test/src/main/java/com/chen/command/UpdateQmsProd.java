package com.chen.command;

import cn.hutool.core.util.StrUtil;
import com.chen.CopyUtils;
import com.chen.GitTool;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author chenwh3
 */
public class UpdateQmsProd {


    public static final String projectSvnPath = "D:\\workspaces\\qms-prod";
    public static final String projectGitPath = "D:\\20221014\\qms-platform";

    public static void copyFile(int rowSize) throws IOException {
        GitTool gitTool = new GitTool();
        gitTool.setPath(projectSvnPath);
        gitTool.exeGit("svn update");
        String oriPath = projectGitPath;
        String aimPath = projectSvnPath;
        CopyUtils.copyFile(oriPath, aimPath, StrUtil.format("git diff --name-status head head~{} ", rowSize));
//        Desktop.getDesktop().open(new File(projectSvnPath));
    }

    public static void main(String[] args) throws IOException {
//        GitTool git = new GitTool();
//        git.setMvn("D:\\\\code\\\\maven\\\\apache-maven-3.8.6\\\\bin\\\\mvn.cmd");
//        git.setSh("C:\\\\Program Files\\\\Git\\\\bin\\\\sh.exe");
//        git.setPath("D:\\20221014\\qms-front\\");
//
//
//        // 1. 构建前端
//        git.exeMvn("mvn clean install -f pom.xml");
//        // 2. 复制文件
//        git.moveFile("sh update-front-prod.sh");
        // 3. 复制后端文件
        copyFile(3);
//        CopyUtils.copyFile("D:\\20221014\\qms-platform", "D:\\workspace", "git diff --name-only head head~1");

        Desktop.getDesktop().open(new File(projectSvnPath));

    }
}
