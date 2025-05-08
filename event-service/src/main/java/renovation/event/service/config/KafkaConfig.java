package renovation.event.service.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import renovation.event.service.kafka.avro.record.work.WorkEvent;
import renovation.event.service.kafka.avro.record.work.WorkEventKey;
import renovation.event.service.kafka.avro.record.worker.WorkerEvent;
import renovation.event.service.kafka.avro.record.worker.WorkerEventKey;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaConfig {

    @Bean
    public NewTopic createEventTopic(@Value("${spring.kafka.topic.event.name}") String topicName) {
        return TopicBuilder.name(topicName)
                .compact()
                .build();
    }

    @Bean
    public KafkaAdmin kafkaAdmin(@Value("${spring.kafka.bootstrap-servers}") String bootstrap) {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        return new KafkaAdmin(configs);
    }

    @Bean
    public KafkaTemplate<WorkEventKey, WorkEvent> workEventKafkaTemplate(
            ProducerFactory<WorkEventKey, WorkEvent> producerFactory) {
        return eventKafkaTemplate(producerFactory);
    }

    @Bean
    public KafkaTemplate<WorkerEventKey, WorkerEvent> workerEventKafkaTemplate(
            ProducerFactory<WorkerEventKey, WorkerEvent> producerFactory) {
        return eventKafkaTemplate(producerFactory);
    }

    private static <K, V> KafkaTemplate<K, V> eventKafkaTemplate(
            ProducerFactory<K, V> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
