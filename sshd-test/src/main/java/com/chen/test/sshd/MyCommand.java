package com.chen.test.sshd;

import lombok.Setter;
import org.apache.sshd.common.util.threads.CloseableExecutorService;
import org.apache.sshd.common.util.threads.ThreadUtils;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author chenwh
 * @date 2021/11/5
 */

public class MyCommand implements Command {

    @Setter
    private InputStream inputStream;
    @Setter
    private OutputStream outputStream;

    @Override
    public void setErrorStream(OutputStream err) {

    }

    @Override
    public void setExitCallback(ExitCallback callback) {

    }
    private CloseableExecutorService temp = ThreadUtils.newFixedThreadPool("temp", 1);

    @Override
    public void start(ChannelSession channel, Environment env) throws IOException {
        System.out.println("start connect .......................");
        temp.submit(()->{
            String command = null;
            System.out.println("fuck ...................");
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                while ((command = reader.readLine()) != null) {
                    handleCmd(outputStream, command);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());

            }
        });
    }

    @Override
    public void destroy(ChannelSession channel) throws Exception {

    }



    public static void handleCmd(OutputStream out, String cmd) throws Exception {
        System.out.println("receive cmd = " + cmd);
        out.write(cmd.getBytes(StandardCharsets.UTF_8));
        out.flush();
    }

    public final void print(){
        System.out.println(123);

    }



    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getContextClassLoader());
    }
}
