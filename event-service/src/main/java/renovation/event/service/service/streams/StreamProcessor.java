/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.event.service.service.streams;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.kafka.streams.StreamsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import renovation.event.service.kafka.avro.record.work.WorkEvent;
import renovation.event.service.kafka.avro.record.work.WorkEventKey;
import renovation.event.service.service.mapper.WorkEventRequestMapper;
import renovation.event.service.web.dto.WorkEventRequest;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class StreamProcessor {
    public static final int CAPACITY = 1;

    private WorkEventRequestMapper workEventRequestMapper;

    @Getter
    private OverwritingQueue<ImmutablePair<UUID, WorkEventRequest>> queue = new OverwritingQueue<>(CAPACITY);

    public StreamProcessor(WorkEventRequestMapper workEventRequestMapper) {
        this.workEventRequestMapper = workEventRequestMapper;
    }

    @Autowired
    public void process(@Value("${spring.kafka.topic.event.name}") String topicName,
                        StreamsBuilder builder) {

        builder.<WorkEventKey, WorkEvent>stream(topicName)
                .foreach(this::extractKeyValueAndPushThemToQueue);
    }

    private void extractKeyValueAndPushThemToQueue(WorkEventKey key, WorkEvent value) {
        var keyOut = UUID.fromString(
                String.valueOf(key.getId())
        );
        var valueOut = workEventRequestMapper.toWorkEventRequest(value);

        var keyValueOut = ImmutablePair.of(
                keyOut,
                valueOut
        );
        queue.offer(keyValueOut);

        log.info("key -> value: {}", keyValueOut);
    }

    public static class OverwritingQueue<E> {
        private final BlockingQueue<E> queue;

        public OverwritingQueue(int capacity) {
            this.queue = new LinkedBlockingQueue<>(capacity);
        }

        public synchronized void offer(E element) {
            if (queue.remainingCapacity() == 0) {
                queue.poll(); // remove oldest element
            }
            queue.offer(element);
        }

        public E poll() {
            return queue.poll();
        }

        public E poll(long timeout, TimeUnit unit) throws InterruptedException {
            return queue.poll(timeout, unit);
        }

        public int size() {
            return queue.size();
        }
    }
}
