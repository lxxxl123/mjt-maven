package com.chen.test.sshd.support;

import io.netty.channel.EventLoopGroup;
import org.apache.sshd.common.io.IoAcceptor;
import org.apache.sshd.common.io.IoHandler;
import org.apache.sshd.netty.NettyIoAcceptor;
import org.apache.sshd.netty.NettyIoServiceFactory;

/**
 * @author chenwh
 * @date 2021/11/12
 */

public class NettyFactory extends NettyIoServiceFactory {
    public NettyFactory() {
    }

    public NettyFactory(EventLoopGroup group) {
        super(group);
    }

    public EventLoopGroup eventLoopGroup(){
        return this.eventLoopGroup;
    }


    @Override
    public IoAcceptor createAcceptor(IoHandler handler) {
        return new MyNettyIoAcceptor(this, handler);
    }

}
