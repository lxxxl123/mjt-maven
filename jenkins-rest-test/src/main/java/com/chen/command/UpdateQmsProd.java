package com.chen.command;

import cn.hutool.core.util.StrUtil;
import com.chen.CopyUtils;
import com.chen.GitObj;
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
        CopyUtils.gitCopy(oriPath, aimPath, StrUtil.format("git diff --name-status head head~{} ", rowSize));
        Desktop.getDesktop().open(new File(projectSvnPath));
    }
    public static final String FRONT_PATH = "D:\\20221014\\qms-front\\";

    public static void buildFront(){
        GitObj frontGit = new GitObj(FRONT_PATH);
        String res = frontGit.git.exeMvn("mvn clean install -f pom.xml");
        if (StrUtil.containsAny(res,"Failure","Command execution failed")) {
            throw new RuntimeException("Build Failure");
        }
    }

    public static void main(String[] args) throws IOException {
        GitTool gitTool = new GitTool();
        gitTool.setPath(projectSvnPath);
        gitTool.exeGit("svn update");

        /*1. 构建前端*/
        buildFront();

        /*2. 复制文件*/
        GitTool git = new GitTool();
        git.setMvn("D:\\\\code\\\\maven\\\\apache-maven-3.8.6\\\\bin\\\\mvn.cmd");
        git.setSh("C:\\\\Program Files\\\\Git\\\\bin\\\\sh.exe");
        git.setPath("D:\\20221014\\qms-front\\");

        git.moveFile("sh update-front-prod.sh");



        // 3. 复制后端文件
        copyFile(2);

        Desktop.getDesktop().open(new File(projectSvnPath));

    }
}
