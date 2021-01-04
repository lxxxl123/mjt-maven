package chen.support;

import chen.model.Message;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * @author chenwh
 * @date 2020/12/30
 */

@Component
@Slf4j
public class MsgSubscriber implements Subscriber<Message> {
    @Getter
    private final AtomicBoolean lock = new AtomicBoolean();

    @Resource(name = "msgConsumer")
    private KafkaConsumer<String, Message> msgConsumer;


    @Override
    public synchronized void subscribe(String topic , Consumer<Message> consumer) {
        log.info("trySubscribeTask topic = {} ", topic);
        subscribeAndPoll(topic, msgConsumer, consumer);
    }

    @Override
    public void unSubscribe() {
        log.info("unSubscribe Task");
        lock.set(false);
    }


}
