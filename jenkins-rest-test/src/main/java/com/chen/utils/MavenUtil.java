package com.chen.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.swing.DesktopUtil;
import cn.hutool.core.util.StrUtil;

import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.util.List;

public class MavenUtil {

    public static String buildDependencyStr(File file, String path){
        if (!"jar".equals(FileUtil.extName(file))) {
            return "";
        }
        String name = StrUtil.removeSuffix(file.getName(), ".jar");
        StringBuilder sb = new StringBuilder();
        sb.append("<dependency>\n");
        sb.append("\t<groupId>com.haday</groupId>\n");
        sb.append("\t<artifactId>").append(name).append("</artifactId>\n");
        sb.append("\t<scope>system</scope>\n");
        sb.append("\t<version>1.0</version>\n");
        sb.append("\t<systemPath>").append(path + file.getName()).append(" </systemPath>\n");
        sb.append("</dependency>");
        return sb.toString();
    }

    public static void main(String[] args) {
        List<File> files = FileUtil.loopFiles("D:\\code\\hip_lib", e -> FileUtil.extName(e).equals("jar"));
        StringBuilder sb = new StringBuilder();
        for (File file : files) {
            String s = buildDependencyStr(file, "${project.basedir}/WebRoot/WEB-INF/lib/");
            sb.append(s + "\n");
        }
        File homeDirectory = FileSystemView.getFileSystemView().getHomeDirectory();
        System.out.println(homeDirectory);

        FileUtil.writeString(sb.toString(), homeDirectory.getAbsolutePath() + "\\" + "text.txt", "utf-8");


    }
}
