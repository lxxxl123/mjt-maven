package com.chen;

import com.chen.dto.DumbAuthUserInfo;
import com.jcraft.jsch.*;
import com.sun.corba.se.pept.transport.ReaderThread;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

/**
 *
 */
@Slf4j
public class Connector {


    private static final Properties options = new Properties();

    static {
        options.put("compression.s2c", "none");
        options.put("compression.c2s", "none");

        options.put("StrictHostKeyChecking", "no");
        options.put("MaxAuthTries", "2");

//        JSch.setlogger(JschSlf4jloggerFactory.logger(log));
    }

    private JSch jsch = new JSch();
    private Session session = null;
    private Channel channel;
    @Getter
    private OutputStream out;
    @Getter
    private InputStream in;

    /**
     *
     * @param host
     * @param port
     * @param username
     * @param password
     * @param timeout ms
     * @throws Exception
     */
    public void doConnect(String host, int port , String username,String password,int timeout ) throws Exception {
        log.info("establish username[{}], host[{}], port[{}]", username, host, port);

        session = jsch.getSession(username, host, port);
        session.setConfig(options);
        session.setTimeout(timeout);
        DumbAuthUserInfo userInfo = new DumbAuthUserInfo(password);
        session.setUserInfo(userInfo);
        session.connect();

        channel = session.openChannel("shell");
        ((ChannelShell) channel).setPtyType("vt100");

        out = channel.getOutputStream();
        in = channel.getInputStream();

        channel.connect();
        interact(in, out);
    }

    public static void interact(InputStream in,OutputStream out ) throws IOException {
        CompletableFuture.runAsync(() -> {
            try {
                byte[] tempbytes = new byte[100];
                int byteread;
                while ((byteread = in.read(tempbytes)) != -1) {
                    System.out.write(tempbytes, 0, byteread);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("end");
        });

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();
            out.write((command+"\r").getBytes(StandardCharsets.UTF_8));
            out.flush();
        }
    }


    public static void main(String[] args) throws Exception {
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String username = args[2];
        String password = args[3];
        int timeout = Integer.parseInt(args[4]);
        Connector connector = new Connector();
        connector.doConnect(host,
                port,
                username,
                password,
                timeout);
        interact(connector.getIn(),connector.getOut());
    }
}
