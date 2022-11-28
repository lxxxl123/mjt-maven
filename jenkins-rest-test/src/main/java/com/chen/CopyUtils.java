package com.chen;

import cn.hutool.core.io.FileUtil;
import com.sun.org.apache.xpath.internal.operations.Or;

import java.io.File;

public class CopyUtils {

    public static void copyFile(String oriPath, String aimPath, String cmd) {
        GitTool gitTool = new GitTool();
        gitTool.setPath(oriPath);
        String files = gitTool.exeGit(cmd);
        String[] fileList = files.trim().split("[\r\n]");
        for (String file : fileList) {
            File f = new File(oriPath + "/" + file);
            FileUtil.copy(f.getAbsolutePath(), aimPath + "/" + file, true);
        }
    }

    public static void main(String[] args) {
        String oriPath = "D:\\20221014\\idea-workspace\\qmsapicenter";
        String aimPath = "D:\\20221014\\qmsApicenter\\qmsApiCenter";
        copyFile(oriPath, aimPath, "git diff --name-only head head~1");
    }
}
