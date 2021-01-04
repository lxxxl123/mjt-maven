package chen.config;


import chen.model.Message;
import chen.serializer.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

/**
 * @author chenwh
 * @date 2020/12/29
 */
@Configuration
@Slf4j
@Import(KafkaProperties.class)
public class KafkaConfig {


    @Bean("stringConsumer")
    public KafkaConsumer<String, String> createKafkaLogConsumer(KafkaProperties kafkaProperties){
        kafkaProperties.getConsumer().setValueDeserializer(StringDeserializer.class);
        kafkaProperties.getConsumer().setGroupId(UUID.randomUUID().toString());
        Map<String, Object> props = kafkaProperties.buildConsumerProperties();
        return new KafkaConsumer<>(props);
    }

    @Bean("msgConsumer")
    public KafkaConsumer<String, Message> createKafkaNeConsumer(KafkaProperties kafkaProperties){
        kafkaProperties.getConsumer().setGroupId(UUID.randomUUID().toString());
        Map<String, Object> props = kafkaProperties.buildConsumerProperties();
        return new KafkaConsumer<>(props, new StringDeserializer(), new JsonDeserializer<>(Message.class));
    }


}
