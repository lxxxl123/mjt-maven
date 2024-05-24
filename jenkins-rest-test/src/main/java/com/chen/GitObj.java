package com.chen;

/**
 * "Lily" , isn't that gonna be hard for her to say ?
 * excuse me , Phill
 * okey , i know that i say I thought this was a bad idea
 * But , uh , what do I know ?
 * I mean , it's not like I wrote the book on fatherhood
 *
 * @author chenwh3
 */
public class GitObj {

    public GitTool git;

    public static final String MVM_PATH = "D:\\\\code\\\\maven\\\\apache-maven-3.8.6\\\\bin\\\\mvn.cmd";
    public static final String GIT_PATH = "C:\\\\Program Files\\\\Git\\\\bin\\\\sh.exe";

    public static final String GIT_COMMIT_AM_TEMP = "git commit -am \"temp\"";

    public GitObj(String path) {
        git = new GitTool();
        git.setCharset("UTF-8");
        git.setMvn(MVM_PATH);
        git.setSh(GIT_PATH);
        git.setPath(path);
    }

    public void setUtf8(){
        git.setCharset("UTF-8");
    }

    public void setGbk(){
        git.setCharset("GBK");
    }


    public GitObj(String path, boolean init) {
        this(path);
        if (init) {
            init();
        }
    }
    public void init(){
        git.exeGit("rm ./.git/index.lock");
        git.exeGit(GIT_COMMIT_AM_TEMP);
        git.exeGit("git clean -f -d");
        git.exeGit("git fetch");
    }


    public void checkOut(String branchName) {
        git.exeGit("git checkout " + branchName);
    }



    public void resetHard(String branchName) {
        git.exeGit("git reset --hard origin/" + branchName);
    }

    public void checkoutResetHardPush(String checkout, String resetHard) {
        git.exeGit("git checkout " + checkout);
        git.exeGit("git reset --hard origin/" + resetHard);
        git.exeGit("git push -f");
    }
}
