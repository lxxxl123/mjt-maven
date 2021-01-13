package chen.support;

import chen.config.KafkaProperties;
import chen.serializer.JsonSerializer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.OffsetSpec;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringSerializer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenwh
 * @date 2020/12/29
 */
@Service
@Slf4j
@Import(KafkaProperties.class)
public class KafkaService {


    @Resource
    private AdminClient adminClient;

    @Resource(name = "kafkaObjectProducer")
    private KafkaProducer<String, Object> kafkaObjectProducer;

    @Bean
    private AdminClient createAdminClient(KafkaProperties kafkaProperties) {

        Map<String, Object> props = kafkaProperties.buildCommonProperties();
        return AdminClient.create(props);
    }

    @Bean("kafkaObjectProducer")
    public KafkaProducer<String, Object> kafkaNeProducer(KafkaProperties kafkaProperties) {
        kafkaProperties.getProducer().setValueSerializer(JsonSerializer.class);
        Map<String, Object> props = kafkaProperties.buildProducerProperties();
        return new KafkaProducer<>(props);
    }

    @Bean("kafkaLogProducer")
    public KafkaProducer<String, String> createKafkaStringProducer(KafkaProperties kafkaProperties) {
        kafkaProperties.getProducer().setValueSerializer(StringSerializer.class);
        Map<String, Object> props = kafkaProperties.buildProducerProperties();
        return new KafkaProducer<>(props);
    }

    //发送消息
    public void send(String topic, Object value) {
        kafkaObjectProducer.send(new ProducerRecord<>(topic, 0, null, value));
    }


    //获取最新
    private Long getLatestOffset(String topic, int partition) {
        TopicPartition topicPartition = new TopicPartition(topic, partition);
        HashMap<TopicPartition, OffsetSpec> map = new HashMap<>();
        map.put(topicPartition, OffsetSpec.latest());
        try {
            return adminClient.listOffsets(map).partitionResult(topicPartition).get().offset();
        } catch (Exception e) {
            log.error("failed to access latest offset , topic = {} , partition = {} ", topic, partition);
            return null;
        }
    }




}
