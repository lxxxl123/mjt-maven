package com.chen.test.sshd;

import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.shell.UnknownCommand;
import org.apache.sshd.server.subsystem.SubsystemFactory;

import java.io.IOException;

/**
 * @author chenwh
 * @date 2021/11/5
 */

public class MyFactory implements SubsystemFactory {
    @Override
    public Command createSubsystem(ChannelSession channelSession) throws IOException {
        UnknownCommand unknownCommand = new UnknownCommand("123");

        return unknownCommand;
    }


    @Override
    public String getName() {
        return "123";
    }
}
