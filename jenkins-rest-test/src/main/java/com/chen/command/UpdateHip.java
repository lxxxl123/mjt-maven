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
    public static final String projectTestGitPath = "D:\\20221014\\HIP";
        public static final String projectGitPath = "D:\\20221014\\hip_git\\hip-qms";

    public static void copyGitToSvn(int rowSize) throws IOException {
        GitTool gitTool = new GitTool();
        String oriPath = projectGitPath;
        String aimPath = projectSvnPath;
        gitTool.setPath(aimPath);
        gitTool.exeGit("svn update");

        CopyUtils.gitCopy(oriPath, aimPath, "git diff --name-status -a head head~" + rowSize);
        Desktop.getDesktop().open(new File(projectSvnPath));
    }

    public static void copyTestToChenwh3(int rowSize) throws IOException {
        GitTool git = new GitTool();
        String oriPath = projectTestGitPath;
        String aimPath = projectGitPath;
        git.setPath(aimPath);
        git.removeIndexLog();
        git.exeGit("git clean -f");
        git.exeGit("git fetch");
        git.exeGit("git reset --hard origin/feature/chenwh3");
        git.exeGit("git rebase origin/master");
        while (rowSize > 0) {
            git.exeGit("git cherry-pick origin/feature/chenwh3~" + rowSize);
            rowSize--;
        }

        Desktop.getDesktop().open(new File(aimPath));
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
     */
    public static void main(String[] args) throws Exception {
//        copyTestToChenwh3(2);
        copyGitToSvn(1);
    }
}
