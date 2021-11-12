package com.chen.test.sshd;

import com.chen.test.sshd.support.NettyFactory;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.common.FactoryManager;
import org.apache.sshd.common.io.AbstractIoServiceFactoryFactory;
import org.apache.sshd.common.io.IoServiceFactory;
import org.apache.sshd.netty.NettyIoServiceFactory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.AbstractCommandSupport;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.shell.ShellFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SshNettyServer {

    private static SshServer sshd;



    public void startServer() throws IOException {
        sshd = SshServer.setUpDefaultServer();
        sshd.setHost("localhost");
        sshd.setPort(22);

        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());

        //Accept all keys for authentication
        sshd.setPublickeyAuthenticator((s, publicKey, serverSession) -> true);

        //Allow username/password authentication using pre-defined credentials
        sshd.setIoServiceFactoryFactory(new AbstractIoServiceFactoryFactory(null) {
            @Override
            public IoServiceFactory create(FactoryManager manager) {
                return new NettyFactory(new NioEventLoopGroup());
            }
        });
        //Allow username/password authentication using pre-defined credentials
        sshd.setPasswordAuthenticator((username, password, serverSession) -> {
            log.info("input username = {} , pwd = {}", username, password);
            return true;
        });

        sshd.setShellFactory(channel ->new NettyCommand());

        sshd.start();
    }

    public static void main(String[] args) throws Exception {
        new SshNettyServer().startServer();
        System.out.println("server started");
        TimeUnit.SECONDS.sleep(10000);
    }

}