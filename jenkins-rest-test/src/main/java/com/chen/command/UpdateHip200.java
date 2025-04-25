package com.chen.command;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.chen.CopyUtils;
import com.chen.GitTool;
import com.chen.JobManager;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chenwh3
 */
@Slf4j
public class UpdateHip200 {


    public static final String projectSvnPath = "D:\\20221014\\qmsApicenter\\qmsApiCenter";
    public static final String projectGitPath = "D:\\20221014\\HIP";


    public static void buildApi() throws IOException, InterruptedException {
        JobManager.buildTrigger("QmsApiCenter_build_develop", "QmsApiCenter_deploy");
    }


    public static void main(String[] args) throws Exception {
        GitTool gitTool = GitTool.ofPath("D:\\20221014\\HIP");
        List<String> allFiles = CopyUtils.getAllFiles(gitTool, StrUtil.format("git diff --name-status -a head~12 head~5"));

        List<String> deleteFile = allFiles.stream().filter(e -> e.startsWith("D")).map(e -> e.substring(1)).collect(Collectors.toList());
        log.info("file size = {}", deleteFile.size());

        String res = deleteFile.stream().map(e -> {
            if (e.startsWith("\t")) {
                e = e.substring(1, e.length());
            }
            if (e.startsWith("WebRoot/") || e.startsWith("\"WebRoot/")) {
                e = e.replace("WebRoot/", "/usr/HIP/tomcat_QMS/webapps/HIP/");
            }
            if (e.startsWith("src/") || e.startsWith("\"src")) {
                e = e.replace("src/", "/usr/HIP/tomcat_QMS/webapps/HIP/WEB-INF/classes/");
                if (e.endsWith(".java")) {
                    e = e.replace(".java", ".class");
                }
            }
            return StrUtil.format("mv {} /tmp/20250311bk", e);
        }).collect(Collectors.joining(" || \\\r\n"));
        FileUtil.writeString(res, "C:\\Users\\chenwh3\\Desktop\\hip_mv.txt", "utf-8");
        System.out.println(res);


    }
}
