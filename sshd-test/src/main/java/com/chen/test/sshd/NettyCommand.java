package com.chen.test.sshd;

import org.apache.sshd.common.io.IoInputStream;
import org.apache.sshd.common.io.IoOutputStream;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.channel.ChannelDataReceiver;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.AsyncCommand;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author chenwh
 * @date 2021/11/8
 */

public class NettyCommand implements AsyncCommand, ChannelDataReceiver {

    private OutputStream out;

    @Override
    public void start(ChannelSession channel, Environment env) throws IOException {

    }


    @Override
    public int data(ChannelSession channel, byte[] buf, int start, int len) throws IOException {
        return 0;
    }


    @Override
    public void close() throws IOException {

    }

    @Override
    public void setIoErrorStream(IoOutputStream err) {

    }

    @Override
    public void setIoInputStream(IoInputStream in) {

    }

    @Override
    public void setIoOutputStream(IoOutputStream out) {

    }

    @Override
    public void setExitCallback(ExitCallback callback) {

    }

    @Override
    public void setErrorStream(OutputStream err) {

    }

    @Override
    public void setInputStream(InputStream in) {

    }

    @Override
    public void setOutputStream(OutputStream out) {
        this.out = out;
    }


    @Override
    public void destroy(ChannelSession channel) throws Exception {

    }
}
