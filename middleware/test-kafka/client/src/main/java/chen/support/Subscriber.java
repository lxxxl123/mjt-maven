package chen.support;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * @author chenwh
 * @date 2020/12/30
 */

public interface Subscriber<T> {

    default void trySubscribe(String topic, Consumer<T> consumer){
        unSubscribe();
        CompletableFuture.runAsync(() -> subscribe(topic, consumer));
    }


    default void subscribeAndPoll(String topic , KafkaConsumer<String,T>kafkaConsumer, Consumer<T> consumer){
        getLock().set(true);
        kafkaConsumer.subscribe(Collections.singleton(topic));
        //按照一定的时间间隔轮询kafka
        while (getLock().get()){
            kafkaConsumer.poll(Duration.ofMillis(500)).forEach(e -> consumer.accept(e.value()));
        }
        kafkaConsumer.unsubscribe();
    }

    AtomicBoolean getLock();

    void subscribe(String topic, Consumer<T> consumer);

    void unSubscribe();
}
