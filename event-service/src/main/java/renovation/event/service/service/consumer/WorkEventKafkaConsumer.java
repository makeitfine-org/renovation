package renovation.event.service.service.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import renovation.event.service.kafka.avro.record.work.WorkEvent;
import renovation.event.service.kafka.avro.record.work.WorkEventKey;

@Component
@Slf4j
public class WorkEventKafkaConsumer extends KafkaConsumer<WorkEventKey, WorkEvent> {

    public WorkEventKafkaConsumer(@Value("${spring.kafka.topic.event.name}") String topicName) {
        super(topicName);
    }

    @Retryable(
            retryFor = Exception.class,
            maxAttempts = DEFAULT_RETRY_MAX_ATTEMPTS + 1,
            backoff = @Backoff(delay = DEFAULT_RETRY_DELAY, maxDelay = DEFAULT_RETRY_MAX_DELAY)
    )
    @KafkaListener(
            topics = "${spring.kafka.topic.event.name}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(ConsumerRecord<WorkEventKey, WorkEvent> consumerRecord) {
        super.kafkaListener(consumerRecord);
    }
}
