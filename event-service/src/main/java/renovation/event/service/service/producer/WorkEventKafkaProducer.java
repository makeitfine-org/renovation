package renovation.event.service.service.producer;

import renovation.event.service.kafka.avro.record.work.WorkEvent;
import renovation.event.service.kafka.avro.record.work.WorkEventKey;

public interface WorkEventKafkaProducer extends KafkaProducer<WorkEventKey, WorkEvent> {
}
