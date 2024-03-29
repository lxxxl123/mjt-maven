package com.chen.test.sshd.support;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.sshd.common.future.CloseFuture;
import org.apache.sshd.common.io.IoAcceptor;
import org.apache.sshd.common.io.IoHandler;
import org.apache.sshd.common.io.IoServiceEventListener;
import org.apache.sshd.common.util.GenericUtils;
import org.apache.sshd.netty.NettyIoAcceptor;
import org.apache.sshd.netty.NettyIoService;
import org.apache.sshd.netty.NettyIoServiceFactory;
import org.apache.sshd.netty.NettyIoSession;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author chenwh
 * @date 2021/11/12
 */

public class MyNettyIoAcceptor extends NettyIoService implements IoAcceptor {

    protected final ServerBootstrap bootstrap = new ServerBootstrap();
    protected final Map<SocketAddress, Channel> boundAddresses = new ConcurrentHashMap<>();

    public MyNettyIoAcceptor(NettyIoServiceFactory fa, IoHandler handler) {
        super(fa, handler);

        if (!(fa instanceof NettyFactory)) {
            throw new RuntimeException("");
        }
        NettyFactory factory = ((NettyFactory) fa);
        channelGroup = new DefaultChannelGroup("sshd-acceptor-channels", GlobalEventExecutor.INSTANCE);
        bootstrap.group(factory.eventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100) // TODO make this configurable
                .handler(new LoggingHandler(LogLevel.INFO)) // TODO make this configurable
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    @SuppressWarnings("synthetic-access")
                    public void initChannel(SocketChannel ch) throws Exception {
                        IoServiceEventListener listener = getIoServiceEventListener();
                        SocketAddress local = ch.localAddress();
                        SocketAddress remote = ch.remoteAddress();
                        SocketAddress service = GenericUtils.head(boundAddresses.keySet());
                        try {
                            if (listener != null) {
                                try {
                                    listener.connectionAccepted(MyNettyIoAcceptor.this, local, remote, service);
                                } catch (Exception e) {
                                    ch.close();
                                    throw e;
                                }
                            }

                            ChannelPipeline p = ch.pipeline();
                            @SuppressWarnings("resource")
                            MyNettyIoSession nettyIoSession = new MyNettyIoSession(MyNettyIoAcceptor.this, handler, service);
                            p.addLast(nettyIoSession.getAdaper());
                        } catch (Exception e) {
                            if (listener != null) {
                                try {
                                    listener.abortAcceptedConnection(MyNettyIoAcceptor.this, local, remote, service, e);
                                } catch (Exception exc) {
                                    if (log.isDebugEnabled()) {
                                        log.debug("initChannel(" + ch + ") listener=" + listener
                                                        + " ignoring abort event exception",
                                                exc);
                                    }
                                }
                            }

                            throw e;
                        }
                    }
                });
    }



    @Override
    public void bind(Collection<? extends SocketAddress> addresses) throws IOException {
        if (GenericUtils.isEmpty(addresses)) {
            return;
        }

        Collection<Channel> bound = new ArrayList<>(addresses.size());
        try {
            for (SocketAddress address : addresses) {
                Channel channel = bindInternal(address);
                bound.add(channel);
            }

            bound.clear(); // disable auto close at finally clause
        } finally {
            for (Channel channel : bound) {
                closeChannel(channel);
            }
        }
    }

    @Override
    public void bind(SocketAddress address) throws IOException {
        bindInternal(address);
    }

    protected Channel bindInternal(SocketAddress address) throws IOException {
        InetSocketAddress inetAddress = (InetSocketAddress) address;
        boolean debugEnabled = log.isDebugEnabled();
        if (debugEnabled) {
            log.debug("bindInternal({}) binding", address);
        }

        ChannelFuture f = bootstrap.bind(inetAddress);
        Channel channel = f.channel();
        channelGroup.add(channel);
        try {
            f.sync();

            SocketAddress bound = channel.localAddress();
            if (debugEnabled) {
                log.debug("bindInternal({}) bound to {}", address, bound);
            }

            Channel prev = boundAddresses.put(bound, channel);
            if (prev != null) {
                if (debugEnabled) {
                    log.debug("bindInternal({}) replaced entry of {} - previous={}",
                            address, bound, prev.localAddress());
                }
            }

            channel.closeFuture().addListener(fut -> boundAddresses.remove(bound));

            // disable auto close at finally clause
            Channel returnValue = channel;
            channel = null;
            return returnValue;
        } catch (InterruptedException e) {
            error("bindInternal({}) interrupted ({}): {}",
                    address, e.getClass().getSimpleName(), e.getMessage(), e);
            throw (InterruptedIOException) new InterruptedIOException(e.getMessage()).initCause(e);
        } finally {
            closeChannel(channel);
        }
    }

    protected void closeChannel(Channel channel) {
        if (channel != null) {
            channelGroup.remove(channel);
            channel.close();
        }
    }

    @Override
    public void unbind(Collection<? extends SocketAddress> addresses) {
        CountDownLatch latch = new CountDownLatch(addresses.size());
        for (SocketAddress address : addresses) {
            Channel channel = boundAddresses.remove(address);
            if (channel != null) {
                ChannelFuture fut;
                if (channel.isOpen()) {
                    fut = channel.close();
                } else {
                    fut = channel.closeFuture();
                }
                fut.addListener(f -> latch.countDown());
            } else {
                latch.countDown();
            }
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void unbind(SocketAddress address) {
        Channel channel = boundAddresses.remove(address);
        if (channel != null) {
            ChannelFuture fut;
            if (channel.isOpen()) {
                fut = channel.close();
            } else {
                fut = channel.closeFuture();
            }
            try {
                fut.sync();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void unbind() {
        Collection<SocketAddress> addresses = getBoundAddresses();
        if (log.isDebugEnabled()) {
            log.debug("Unbinding {}", addresses);
        }

        unbind(addresses);
    }

    @Override
    public Set<SocketAddress> getBoundAddresses() {
        return new HashSet<>(boundAddresses.keySet());
    }

    @Override
    protected CloseFuture doCloseGracefully() {
        channelGroup.close().addListener(fut -> closeFuture.setClosed());
        return closeFuture;
    }

    @Override
    protected void doCloseImmediately() {
        doCloseGracefully();
        super.doCloseImmediately();
    }

}
