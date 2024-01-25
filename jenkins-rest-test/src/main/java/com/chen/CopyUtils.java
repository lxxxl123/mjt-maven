package com.chen;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CopyUtils {

    public static void gitCopy(String oriPath, String aimPath, String cmd) {
        GitTool gitTool = new GitTool();
        gitTool.setPath(oriPath);
        String fileText = gitTool.exeGit(cmd);
        String[] fileList = fileText.trim().split("[\r\n]");

        List<String> files = new ArrayList<>(fileList.length);
        for (String file : fileList) {
            String[] split = file.split("\\s+");
            for (int i = 1; i < split.length; i++) {
                files.add(split[i]);
            }
        }

        for (String file : files) {
            System.out.println(file);
            File f = new File(oriPath + "/" + file);
            File aim = new File(aimPath + "/" + file);
            if (f.exists()) {
                FileUtil.copy(f.getAbsolutePath(), aim.getAbsolutePath(), true);
            } else {
                aim.delete();
            }
        }
    }


}
