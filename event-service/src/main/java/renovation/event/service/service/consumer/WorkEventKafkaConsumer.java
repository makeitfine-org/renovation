package renovation.event.service.service.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
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

    public static final int RETRY_MAX_ATTEMPTS = 3;
    public static final int RETRY_DELAY = 1000;
    public static final int RETRY_MAX_DELAY = 3000;

    private BlockingQueue<ImmutablePair<WorkEventKey, WorkEvent>> q =
            new LinkedBlockingQueue<>(LAST_ELEMENT_SAVING_QUEUE_CAPACITY);

    // todo: consider "Scaling Event-Driven Microservices"
    // https://medium.com/@bubu.tripathy/event-driven-architecture-adb658a1dc9c
    @Retryable(
            retryFor = Exception.class,
            maxAttempts = RETRY_MAX_ATTEMPTS,
            backoff = @Backoff(delay = RETRY_DELAY, maxDelay = RETRY_MAX_DELAY)
    )
    @KafkaListener(
            topics = "${spring.kafka.topic.event.name}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(ConsumerRecord<WorkEventKey, WorkEvent> consumerRecord) {
        log.info("Received: {}", consumerRecord);

        q.clear();
        q.offer(ImmutablePair.of(consumerRecord.key(), consumerRecord.value()));

        // todo: implement dead-letter queue for messages with a failed to process:
        // https://medium.com/@bubu.tripathy/event-driven-architecture-adb658a1dc9c
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
