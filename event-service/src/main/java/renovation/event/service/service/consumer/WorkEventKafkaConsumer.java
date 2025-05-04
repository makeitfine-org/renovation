package renovation.event.service.service.consumer;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import renovation.event.service.kafka.avro.record.work.WorkEvent;
import renovation.event.service.kafka.avro.record.work.WorkEventKey;

@Component
@Slf4j
public class WorkEventKafkaConsumer {

    @Getter
    private ImmutablePair<WorkEventKey, WorkEvent> lastWorkEventKeyValue;

    @KafkaListener(
            topics = "${spring.kafka.topic.name}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(ConsumerRecord<WorkEventKey, WorkEvent> consumerRecord) {
        log.info("Received: {}", consumerRecord);

        lastWorkEventKeyValue = ImmutablePair.of(consumerRecord.key(), consumerRecord.value());
    }
}
