package com.chen.test.sshd.server;

import com.chen.test.sshd.MyCommand;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;

import java.io.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SSHTest1Server {

    private static SshServer sshd;


    public void startServer() throws IOException {
        sshd = SshServer.setUpDefaultServer();
        sshd.setHost("localhost");
        sshd.setPort(22);

        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());

        //Accept all keys for authentication
        sshd.setPublickeyAuthenticator((s, publicKey, serverSession) -> true);

        //Allow username/password authentication using pre-defined credentials
        sshd.setPasswordAuthenticator((username, password, serverSession) -> true);

        sshd.setShellFactory(channel -> new MyCommand());


        sshd.start();
    }


    public static void main(String[] args) throws Exception {
        new SSHTest1Server().startServer();
        System.out.println("server started");
        TimeUnit.SECONDS.sleep(10000);
    }

}