package com.chen;

public class updateProd {

    public static void main(String[] args) {
        GitTool git = new GitTool();
        git.setMvn("D:\\\\code\\\\maven\\\\apache-maven-3.8.6\\\\bin\\\\mvn.cmd");
        git.setSh("C:\\\\Program Files\\\\Git\\\\bin\\\\sh.exe");
        git.setPath("D:\\20221014\\qms-front\\");


        git.exeMvn("mvn clean install -f pom.xml");
        git.moveFile("sh update-front-prod.sh");
    }
}
