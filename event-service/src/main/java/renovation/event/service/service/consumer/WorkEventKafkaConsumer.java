package renovation.event.service.service.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import renovation.event.service.kafka.avro.record.work.WorkEvent;
import renovation.event.service.kafka.avro.record.work.WorkEventKey;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class WorkEventKafkaConsumer {

    public static final int LAST_ELEMENT_SAVING_QUEUE_CAPACITY = 1;
    public static final int DEFAULT_LAST_ELEMENT_READ_WAIT_TIME_MILLIS = 5000;

    private BlockingQueue<ImmutablePair<WorkEventKey, WorkEvent>> q =
            new LinkedBlockingQueue<>(LAST_ELEMENT_SAVING_QUEUE_CAPACITY);

    @KafkaListener(
            topics = "${spring.kafka.topic.name}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(ConsumerRecord<WorkEventKey, WorkEvent> consumerRecord) {
        log.info("Received: {}", consumerRecord);

        q.clear();
        q.offer(ImmutablePair.of(consumerRecord.key(), consumerRecord.value()));
    }

    /**
     * Wait up to 5 seconds while kafka listener take last element.
     *
     * @return WorkEventKeyValue element
     * @throws InterruptedException
     */
    public ImmutablePair<WorkEventKey, WorkEvent> pollLastWorkEventKeyValue() throws InterruptedException {
        return pollLastWorkEventKeyValue(DEFAULT_LAST_ELEMENT_READ_WAIT_TIME_MILLIS);
    }


    /**
     * Wait up to {@code millis} milliseconds while kafka listener take last element.
     *
     * @param millis milliseconds to wait until read
     * @return WorkEventKeyValue element
     * @throws InterruptedException
     */
    public ImmutablePair<WorkEventKey, WorkEvent> pollLastWorkEventKeyValue(int millis) throws InterruptedException {
        return q.poll(millis, TimeUnit.SECONDS);
    }
}
