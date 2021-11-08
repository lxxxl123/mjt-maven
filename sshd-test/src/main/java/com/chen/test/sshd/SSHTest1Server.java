package com.chen.test.sshd;

import lombok.Setter;
import org.apache.sshd.common.util.threads.CloseableExecutorService;
import org.apache.sshd.common.util.threads.ThreadUtils;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.shell.ShellFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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