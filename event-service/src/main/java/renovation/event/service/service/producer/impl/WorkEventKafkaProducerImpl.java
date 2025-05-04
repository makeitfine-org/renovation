package renovation.event.service.service.producer.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import renovation.event.service.kafka.avro.record.work.WorkEvent;
import renovation.event.service.kafka.avro.record.work.WorkEventKey;
import renovation.event.service.service.producer.WorkEventKafkaProducer;

@Service
public class WorkEventKafkaProducerImpl extends KafkaProducerImpl<WorkEventKey, WorkEvent>
        implements WorkEventKafkaProducer {

    public WorkEventKafkaProducerImpl(
            KafkaTemplate<WorkEventKey, WorkEvent> kafkaTemplate,
            @Value("${spring.kafka.topic.name}") String topicName
    ) {
        super(kafkaTemplate, topicName);
    }
}
