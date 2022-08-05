package com.chen;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.RuntimeUtil;
import lombok.Cleanup;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.SQLOutput;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Data
public class GitTool {

    private File file;

    private static final Pattern BRANCH_PATTERN = Pattern.compile("\\*\\s([a-zA-Z.\\-/\\d]+)");

    private static final Pattern CHECKOUT_PATTERN = Pattern.compile("(Switched to branch)|(Already on)|(Your branch is behind)");

    private String branch;

    public String mvn;

    public String sh = "";

    public GitTool() {
//        checkBranch();
    }

    public void setPath(String path) {
        this.file = new File(path);
    }

    public static String readInputStreamtoConsole(InputStream inputStream, String charset) throws IOException {
        byte[] bytes = new byte[1024];
        int read;
        StringBuffer sb = new StringBuffer();
        @Cleanup
        InputStream i = inputStream;
        while ((read = i.read(bytes)) > -1) {
            String s = new String(bytes, 0, read, charset);
            System.out.print(s);
            sb.append(s);
        }
        return sb.toString();
    }

    public static String getResult(Process process) {
        String charset = "gbk";
        try {
            String r1 = readInputStreamtoConsole(process.getInputStream(), charset);
            String r2 = readInputStreamtoConsole(process.getErrorStream(), charset);
            if (StringUtils.isBlank(r2)) {
                return r1;
            }
            return r1 + "\n" + r2;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            RuntimeUtil.destroy(process);
        }
    }

    public String exeGit(String cmd) {
        log.info("cmd = {}", cmd);
        Process exec = RuntimeUtil.exec(null, file, cmd);
        String result = getResult(exec);
//        log.info("cmd = {} , res = \n{}", cmd, result);
        return result;
    }

    public String exeMvn(String cmd) {
        log.info("cmd = {}", cmd);
        cmd = cmd.replaceFirst("mvn", mvn);
        Process exec = RuntimeUtil.exec(null, file, cmd);
        String result = getResult(exec);
//        log.info("cmd = {} , res = \n{}", cmd, result);
        return result;
    }

    public String exeSh(String cmd, File path) {
        log.info("cmd = {}", cmd);
        cmd = cmd.replaceFirst("sh", sh);
        Process exec = RuntimeUtil.exec(null, path, cmd);
        String result = getResult(exec);
//        log.info("cmd = {} , res = \n{}", cmd, result);
        return result;
    }


    public String moveFile() {
        File file = new File((GitTool.class.getResource("/").getFile()));
        String cmd = "sh update-front.sh";
        return exeSh(cmd, file);
    }


    public String checkout(String branch) {
        String res = exeGit("git checkout " + branch);
        Matcher matcher = CHECKOUT_PATTERN.matcher(res);
        if (!matcher.find()) {
            throw new RuntimeException(String.format("check out branch = %s fail", branch));
        }
        this.branch = branch;
        return branch;
    }

    public String checkBranch() {
        String res = exeGit("git branch");
        String curBranch = ReUtil.extractMulti(BRANCH_PATTERN, res, "$1");
        if (StringUtils.isBlank(curBranch)) {
            throw new RuntimeException(String.format("check out branch = %s fail", branch));
        }
        this.branch = curBranch;
        log.info("curBranch = {}", branch);
        return curBranch;
    }

    public static String brandName = "feature/market-complainV1.0.0";

    public static void main(String[] args) throws IOException, InterruptedException {
        GitTool git = new GitTool();
        String frontEndName = brandName + "-front-end";
        git.setMvn("D:\\\\program\\\\apache-maven-3.8.5\\\\bin\\\\mvn.cmd");
        git.setSh("C:\\\\Program Files\\\\Git\\\\bin\\\\sh.exe");
        git.setPath("C:\\Users\\chenwh3\\IdeaProjects\\qms-front\\");
        //build
        git.exeMvn("mvn clean install -f pom.xml");

        git.setPath("C:\\Users\\chenwh3\\IdeaProjects\\qms-platform\\");

        git.exeGit("git commit -am \"temp\"");
        git.checkout(brandName);
        git.exeGit("git commit -am \"temp\"");
        git.exeGit("git branch " + frontEndName);
        git.checkout(frontEndName);
        git.exeGit("git reset --hard " + brandName);
        git.exeGit("git reset --hard head~1");
        git.moveFile();
        git.exeGit("git add \"qms-service/src/main/resources/static/*\"");
        git.exeGit("git commit -am \"temp\"");
        git.exeGit("git push --force");


        git.exeGit("git checkout " + brandName);
        git.exeGit("git reset --soft head~1");

//        JobManager.buildAndDeployQmsPlatform(frontEndName);

    }
}
