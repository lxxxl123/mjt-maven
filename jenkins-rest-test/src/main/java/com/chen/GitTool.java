package com.chen;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.RuntimeUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Data
public class GitTool {

    private File file ;

    private static final Pattern BRANCH_PATTERN = Pattern.compile("\\*\\s([a-zA-Z.\\-/\\d]+)");

    private static final  Pattern CHECKOUT_PATTERN = Pattern.compile("(Switched to branch)|(Already on)|(Your branch is behind)");

    private String branch;

    public String mvn;

    public String sh = "";

    public GitTool(){
//        checkBranch();
    }

    public void setPath(String path){
        this.file = new File(path);
    }


    public static String getResult(Process process) {
        Charset charset = Charset.forName("utf-8");
        InputStream in = null;
        try {
            InputStream inputStream = process.getInputStream();
            byte[] bytes = new byte[1024];
            int read ;
            StringBuffer sb = new StringBuffer();
            while ((read = inputStream.read(bytes)) > -1) {
                String s = new String(bytes, 0, read, "gbk");
                System.out.print(s);
                sb.append(s);
            }
            String r1 = IoUtil.read(process.getErrorStream(), charset);
            String r2 = sb.toString();

            if (StringUtils.isBlank(r1)) {
                return r2;
            }
            return r1 + "\n" + r2;
        } catch (IOException e) {
            throw new RuntimeException(e);
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

    public String exeMvn(String cmd){
        cmd = cmd.replaceFirst("mvn", mvn);
        Process exec = RuntimeUtil.exec(null, file, cmd);
        String result = getResult(exec);
        log.info("cmd = {} , res = \n{}", cmd, result);
        return result;
    }

    public String exeSh(String cmd,String path){
        cmd = cmd.replaceFirst("sh", sh);
        Process exec = RuntimeUtil.exec(null, file, cmd);
        String result = getResult(exec);
        log.info("cmd = {} , res = \n{}", cmd, result);
        return result;
    }


    public String moveFile(){
        File file = new File(GitTool.class.getResource("/").getFile());
        String cmd = "sh.exe update-front.sh";
        return exeSh(cmd, file.getAbsolutePath());
    }


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

    public static String brandName = "feature/market-complainV1.0.0";

    public static void main(String[] args) throws IOException, InterruptedException {
        GitTool git = new GitTool();
        git.setMvn("D:\\\\program\\\\apache-maven-3.8.5\\\\bin\\\\mvn.cmd");
        git.setSh("C:\\\\Program Files\\\\Git\\\\bin\\\\sh.exe");

        git.setPath("C:\\Users\\chenwh3\\IdeaProjects\\qms-front");
        git.exeMvn("mvn clean install -f pom.xml");

        git.setPath("C:\\Users\\chenwh3\\IdeaProjects\\qms-platform\\");
        String frontEndName = brandName + "-front-end";
        git.exeGit("git commit -am \"temp\"");
        git.checkout(brandName);
        git.exeGit("git commit -am \"temp\"");
        git.exeGit("git branch " + frontEndName);
        git.checkout(frontEndName);
        git.exeGit("git reset --hard " + brandName);
        git.moveFile();
        git.exeGit("git add \"qms-service/src/main/resources/static/*\"");
        git.exeGit("git commit -am \"temp\"");
        git.exeGit("git push --force");



//        git.exeGit("git checkout " + brandName);
//        git.exeGit("git stash apply stash@{0}");

        Session session = new Session();
        session.buildAndDeploy(frontEndName,"http://192.168.26.2:8080");

    }
}
