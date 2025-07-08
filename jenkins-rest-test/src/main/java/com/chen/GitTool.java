package com.chen;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.RuntimeUtil;
import lombok.Cleanup;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.sql.rowset.serial.SerialException;
import java.io.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chenwh3
 */
@Slf4j
@Data
public class GitTool {

    public static final String CMD = "cmd = {}";

    private File file;

    private static final Pattern BRANCH_PATTERN = Pattern.compile("\\*\\s([a-zA-Z.\\-/\\d]+)");

    private static final Pattern CHECKOUT_PATTERN = Pattern.compile("(Switched to (a new)?\\s*branch)|(Already on)|(Your branch is behind)");

    private String branch;

    protected String mvn;

    private String charset = "gbk";

    protected String sh = "";

    public GitTool() {
//        checkBranch();
    }

    public static GitTool ofPath(String path) {
        GitTool gitTool = new GitTool();
        gitTool.setPath(path);
        return gitTool;

    }


    public void setPath(String path) {
        log.info("set current path = {}", path);
        this.file = new File(path);
    }

    public static CompletableFuture<String> readInputStreamtoConsole(InputStream inputStream, String charset) throws IOException {
        return readInputStreamtoConsole(inputStream, charset, "");
    }
    public static CompletableFuture<String> readInputStreamtoConsole(InputStream inputStream, String charset, String prefix) throws IOException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                StringBuilder sb = new StringBuilder();
                @Cleanup
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(prefix + line);
                    sb.append(line);
                    sb.append("\r\n");
                }
                return sb.toString();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public String getResult(Process process) {
        String charset = this.charset;
        try {
            CompletableFuture<String> c1 = readInputStreamtoConsole(process.getInputStream(), charset);
            CompletableFuture<String> c2 = readInputStreamtoConsole(process.getErrorStream(), charset, "[ERROR]");
            String r2 = c2.join();
            String r1 = c1.join();
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

    public String removeIndexLog(){
        return exeGit("rm ./.git/index.lock");
    }

    public String merge(String branch) {
        String res = exeGit("git merge " + branch);
        if (res.contains("merge failed")) {
            throw new RuntimeException("合并失败");
        }
        return res;
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


    public String moveFile(String cmd) {
        File cFile = new File((GitTool.class.getResource("/").getFile()));
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

}
