package com.chen;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.RuntimeUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Data
public class GitTool {

    public String path = "C:\\Users\\chenwh3\\IdeaProjects\\qms-platform\\";
    public File file = new File(path);

    public String branch;

    public GitTool(){
        checkBranch();
    }


    public static String getResult(Process process) {
        Charset charset = Charset.forName("utf-8");
        InputStream in = null;
        try {
            in = process.getInputStream();

            String r1 = IoUtil.read(process.getErrorStream(), charset);
            String r2 = IoUtil.read(process.getInputStream(), charset);
            if (StringUtils.isBlank(r1)) {
                return r2;
            }
            return r1 + "\n" + r2;
        } finally {
            IoUtil.close(in);
            RuntimeUtil.destroy(process);
        }
    }

    public String exeGit(String cmd){
        Process exec = RuntimeUtil.exec(null, file, cmd);
        String result = getResult(exec);
        log.info("cmd = {} , res = \n{}", cmd, result);
        return result;
    }

    public static String moveFile(){
        File file = new File(GitTool.class.getResource("/").getFile());
        String cmd = "C:\\Program Files\\Git\\bin\\sh.exe update-front.sh";
        String result = RuntimeUtil.getResult(RuntimeUtil.exec(null, file, cmd));
        log.info("cmd = {} , res = {}", cmd, result);
        return result;
    }


    private Pattern BRANCH_PATTERN = Pattern.compile("\\*\\s([a-zA-Z.\\-/0-9]+)");
    private Pattern CHECKOUT_PATTERN = Pattern.compile("(Switched to branch)|(Already on)|(Your branch is behind)");



    public String checkout(String branch){
        String res = exeGit("git checkout " + branch);
        Matcher matcher = CHECKOUT_PATTERN.matcher(res);
        if(!matcher.find()) {
            throw new RuntimeException(String.format("check out branch = %s fail", branch));
        }
        this.branch = branch;
        return branch;
    }

    public String checkBranch(){
        String res = exeGit("git branch");
        String curBranch = ReUtil.extractMulti(BRANCH_PATTERN, res, "$1");
        if (StringUtils.isBlank(curBranch)) {
            throw new RuntimeException(String.format("check out branch = %s fail", branch));
        }
        this.branch = curBranch;
        log.info("curBranch = {}", branch);
        return curBranch;
    }

    public static void main(String[] args) {
        GitTool git = new GitTool();
        String brandName = "feature/market-complainV1.0.0";
        String frontEndName = brandName + "-front-end";
        git.checkout(frontEndName);
        git.exeGit("git commit -am \"temp\"");
        git.exeGit("git branch " + brandName);
        git.exeGit("git checkout " + frontEndName);
        git.exeGit("git reset --hard " + brandName);
        moveFile();
        git.exeGit("git add \"qms-service/src/main/resources/static/*\"");
        git.exeGit("git commit -am \"temp\"");
        git.exeGit("git push --force");
        git.exeGit("git checkout " + brandName);
        git.exeGit("git stash apply stash@{0}");
    }
}
