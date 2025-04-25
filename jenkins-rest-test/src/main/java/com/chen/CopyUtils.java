package com.chen;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CopyUtils {

    public static List<String> getAllFiles(GitTool git, String cmd) {
        String fileText = git.exeGit(cmd);
        return Arrays.stream(fileText.trim().split("[\r\n]"))
                .map(String::trim)
                .filter(StrUtil::isNotBlank).collect(Collectors.toList());
    }

    public static void gitCopy(String oriPath, String aimPath, String cmd) {
        GitTool gitTool = new GitTool();
        gitTool.setPath(oriPath);
        List<String> fileList = getAllFiles(gitTool, cmd);

        List<String> files = new ArrayList<>(fileList.size());

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
