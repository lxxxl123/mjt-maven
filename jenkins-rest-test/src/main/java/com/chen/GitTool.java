package com.chen;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.RuntimeUtil;
import lombok.Cleanup;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chenwh3
 */
@Slf4j
@Data
public class GitTool {

    public static final String CMD = "cmd = {}";
    public static final String GIT_COMMIT_AM_TEMP = "git commit -am \"temp\"";
    private File file;

    private static final Pattern BRANCH_PATTERN = Pattern.compile("\\*\\s([a-zA-Z.\\-/\\d]+)");

    private static final Pattern CHECKOUT_PATTERN = Pattern.compile("(Switched to branch)|(Already on)|(Your branch is behind)");

    private String branch;

    protected String mvn;

    private String charset = "gbk";

    protected String sh = "";

    public GitTool() {
//        checkBranch();
    }

    public void setPath(String path) {
        this.file = new File(path);
    }

    public static String readInputStreamtoConsole(InputStream inputStream, String charset) throws IOException {
        byte[] bytes = new byte[1024];
        int read;
        StringBuilder sb = new StringBuilder();
        @Cleanup
        InputStream i = inputStream;
        while ((read = i.read(bytes)) > -1) {
            String s = new String(bytes, 0, read, charset);
            System.out.println(s);
            sb.append(s);
        }
        return sb.toString();
    }

    public String getResult(Process process) {
        String charset = this.charset;
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
        log.info(CMD, cmd);
        Process exec = RuntimeUtil.exec(null, file, cmd);
        return getResult(exec);
    }

    public String exeMvn(String cmd) {
        log.info(CMD, cmd);
        cmd = cmd.replaceFirst("mvn", mvn);
        Process exec = RuntimeUtil.exec(null, file, cmd);
        return getResult(exec);
    }

    public String exeSh(String cmd, File path) {
        log.info(CMD, cmd);
        cmd = cmd.replaceFirst("sh", sh);
        Process exec = RuntimeUtil.exec(null, path, cmd);
        return getResult(exec);
    }


    public String moveFile() {
        File cFile = new File((GitTool.class.getResource("/").getFile()));
        String cmd = "sh update-front.sh";
        return exeSh(cmd, cFile);
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

//    public static final String BRAND_NAME = "feature/market-complainV1.0.0";
//    public static final String BRAND_NAME = "feature/chargReport-v1.0.0";
//    public static final String BRAND_NAME = "feature/sampleCheckIn-v1.0.0";

    public static final String BRAND_NAME = "feature/qalsData-V1.0.0";


    public static void build() throws Exception {

        GitTool git = new GitTool();
        git.setCharset("gbk");
        String frontEndName = BRAND_NAME + "-front-end";
        git.setMvn("D:\\\\code\\\\maven\\\\apache-maven-3.8.6\\\\bin\\\\mvn.cmd");
        git.setSh("C:\\\\Program Files\\\\Git\\\\bin\\\\sh.exe");
        git.setPath("D:\\20221014\\qms-front\\");
        //build front end
//        git.exeMvn("mvn clean install -f pom.xml");


        git.setPath("D:\\20221014\\qms-platform\\");

        git.exeGit(GIT_COMMIT_AM_TEMP);
        git.checkout(BRAND_NAME);
        git.exeGit(GIT_COMMIT_AM_TEMP);
        git.exeGit("git rebase origin/master");
        git.exeGit("git branch " + frontEndName);
        git.checkout(frontEndName);
        git.exeGit("git reset --hard " + BRAND_NAME);
//        git.exeGit("git reset --hard head~1");
        git.moveFile();
        git.exeGit("git add \"qms-service/src/main/resources/static/*\"");
        git.exeGit(GIT_COMMIT_AM_TEMP);
        git.exeGit("git push --force");


        git.exeGit("git checkout -f " + BRAND_NAME);
//        git.exeGit("git reset --soft head~1");

        JobManager.buildAndDeployQmsPlatform(frontEndName);
    }

    public static void main(String[] args) throws Exception{
        build();

    }
}
