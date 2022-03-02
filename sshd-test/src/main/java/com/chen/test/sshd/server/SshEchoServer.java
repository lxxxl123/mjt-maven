package com.chen.test.sshd.server;

import com.chen.test.sshd.NettyCommand;
import lombok.SneakyThrows;
import org.apache.sshd.common.keyprovider.ClassLoadableResourceKeyPairProvider;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.AbstractCommandSupport;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.command.CommandFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.shell.ShellFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class SshEchoServer {

    private static SshServer sshd;



    public void startServer() throws IOException {
        sshd = SshServer.setUpDefaultServer();
        sshd.setPort(22);

        ClassLoadableResourceKeyPairProvider keyProvider
                = new ClassLoadableResourceKeyPairProvider("META-INF/ssh-server.pem");
        sshd.setKeyPairProvider(keyProvider);

        //Accept all keys for authentication
        sshd.setPublickeyAuthenticator((s, publicKey, serverSession) -> true);

        //Allow username/password authentication using pre-defined credentials
        sshd.setPasswordAuthenticator((username, password, serverSession) ->  true);

        sshd.setCommandFactory(new CommandFactory() {
            @Override
            public Command createCommand(ChannelSession channel, String command) throws IOException {
                return new NettyCommand();
            }
        });

        sshd.setShellFactory(new ShellFactory() {
            @Override
            public Command createShell(ChannelSession channel) throws IOException {
                return new AbstractCommandSupport(null, null) {
                    @SneakyThrows
                    @Override
                    public void run() {
                        String command = getCommand();
                        if (command == null) {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(getInputStream()));
                            while ((command = reader.readLine()) != null) {
                                handleCmd(command);
                            }
                        }else{
                            handleCmd(command);
                        }
                    }

                    public void handleCmd(String cmd) throws Exception{
                        System.out.println("receive cmd = " + cmd);
                        getOutputStream().write(cmd.getBytes(StandardCharsets.UTF_8));
                        getOutputStream().flush();
                    }
                };
            }
        });

        sshd.start();
    }

    public static void main(String[] args) throws Exception {
        new SshEchoServer().startServer();
        System.out.println("server started");
        TimeUnit.SECONDS.sleep(10000);
    }

}