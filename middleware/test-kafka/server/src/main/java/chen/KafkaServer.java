package chen;

import chen.config.SpringUtils;
import chen.model.Message;
import chen.support.KafkaService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author chenwh
 * @date 2021/1/4
 */
@SpringBootApplication
@EnableConfigurationProperties
public class KafkaServer {


    public static void main(String[] args) {

        SpringApplication.run(KafkaServer.class);
        KafkaService kafkaService = SpringUtils.getBean(KafkaService.class);
        Message message = new Message();
        message.setId(123);
        message.setMessage("abcd");

        kafkaService.send("topic-test",message );


    }

}
