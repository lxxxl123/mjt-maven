package chen.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author chenwh
 * @date 2021/2/22
 */
@Slf4j
public class IdleHandler extends IdleStateHandler {

    public IdleHandler(long readerIdleTime, long writerIdleTime, long allIdleTime, TimeUnit unit) {
        super(readerIdleTime, writerIdleTime, allIdleTime, unit);
    }


    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) {
        if (evt != null) {
            IdleState state = evt.state();
            log.error("超时 {}",evt);
            log.error("{}",state);
            throw new RuntimeException("超时");
        }
    }
}
