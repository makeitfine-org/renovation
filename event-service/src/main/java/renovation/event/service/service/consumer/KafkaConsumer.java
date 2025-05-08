package renovation.event.service.service.consumer;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class KafkaConsumer<K, V> {

    public static final int LAST_ELEMENT_SAVING_QUEUE_CAPACITY = 1;
    public static final int DEFAULT_LAST_ELEMENT_READ_WAIT_TIME_MILLIS = 5000;

    protected static final int DEFAULT_RETRY_MAX_ATTEMPTS = 3;
    protected static final int DEFAULT_RETRY_DELAY = 1000;
    protected static final int DEFAULT_RETRY_MAX_DELAY = 3000;

    private final String listenerPrefixMessage;

    /**
     * One element blocking queue.
     */
    @Getter(AccessLevel.PROTECTED)
    private BlockingQueue<ImmutablePair<K, V>> q =
            new LinkedBlockingQueue<>(LAST_ELEMENT_SAVING_QUEUE_CAPACITY);

    public KafkaConsumer(String listenerPrefixMessage) {
        this.listenerPrefixMessage = listenerPrefixMessage;
    }

    /**
     * Call method inside listener.
     *
     * @param consumerRecord
     */
    // todo: consider "Scaling Event-Driven Microservices"
    // https://medium.com/@bubu.tripathy/event-driven-architecture-adb658a1dc9c
    protected void kafkaListener(ConsumerRecord<K, V> consumerRecord) {
        log.info("{} {}", listenerPrefixMessage, consumerRecord);

        q.clear();
        q.offer(ImmutablePair.of(consumerRecord.key(), consumerRecord.value()));

        // todo: implement dead-letter queue for messages with a failed to process:
        // https://medium.com/@bubu.tripathy/event-driven-architecture-adb658a1dc9c
    }

    /**
     * Wait up to 5 seconds while kafka listener take last element.
     *
     * @return KeyValue element
     * @throws InterruptedException
     */
    public ImmutablePair<K, V> pollLastKeyValue() throws InterruptedException {
        return pollLastKeyValue(DEFAULT_LAST_ELEMENT_READ_WAIT_TIME_MILLIS);
    }

    /**
     * Wait up to {@code millis} milliseconds while kafka listener take last element.
     *
     * @param millis milliseconds to wait until read
     * @return KeyValue element
     * @throws InterruptedException
     */
    public ImmutablePair<K, V> pollLastKeyValue(int millis) throws InterruptedException {
        return q.poll(millis, TimeUnit.MILLISECONDS);
    }
}
