package renovation.event.service.service.producer.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import renovation.event.service.service.producer.KafkaProducer;

@Slf4j
@AllArgsConstructor
public abstract class KafkaProducerImpl<K, V> implements KafkaProducer<K, V> {

    private KafkaTemplate<K, V> kafkaTemplate;

    private String topicName;

    @Override
    public void send(V data) {
        kafkaTemplate.send(topicName, data);
    }

    @Override
    public void send(K key, V data) {
        kafkaTemplate.send(topicName, key, data);
    }

    @Override
    public void send(K key, V data, String specifiedTopic) {
        kafkaTemplate.send(specifiedTopic, key, data);
    }
}
