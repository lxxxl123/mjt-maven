package chen;

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
    }

}
