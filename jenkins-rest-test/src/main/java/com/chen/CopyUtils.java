package com.chen;

import cn.hutool.core.io.FileUtil;

import java.io.File;

public class CopyUtils {

    public static void main(String[] args) {
        GitTool gitTool = new GitTool();
        String oriPath = "D:\\20221014\\idea-workspace\\qmsapicenter";
        String aimPath = "D:\\20221014\\qmsApicenter\\qmsApiCenter";
        gitTool.setPath(oriPath);
        String files = gitTool.exeGit("git diff --name-only head head~1");
        String[] fileList = files.trim().split("[\r\n]");
        for (String file : fileList) {
            File f = new File(oriPath + "/" + file);
            FileUtil.copy(f.getAbsolutePath(), aimPath + "/" + file, true);
        }

    }
}
