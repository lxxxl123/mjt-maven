package chen;

import chen.config.SpringUtils;
import chen.support.MsgSubscriber;
import chen.support.StrSubscriber;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author chenwh
 * @date 2021/1/4
 */
@SpringBootApplication
@EnableConfigurationProperties
public class KafkaClient {
    public static void main(String[] args) {
        SpringApplication.run(KafkaClient.class);
        MsgSubscriber msgSubscriber = SpringUtils.getBean(MsgSubscriber.class);
        StrSubscriber strSubscriber = SpringUtils.getBean(StrSubscriber.class);
        msgSubscriber.trySubscribe("topic-test", e -> System.out.println("接收到消息" + e));
//        strSubscriber.trySubscribe("topic-test", e -> System.out.println("接收到消息" + e));
    }
}
