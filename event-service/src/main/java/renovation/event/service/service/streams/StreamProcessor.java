/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.event.service.service.streams;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
@Slf4j
public class StreamProcessor {
    public static final int CAPACITY = 10;
    private OverwritingQueue<ImmutablePair<String, String>> queue = new OverwritingQueue<>(CAPACITY);

    @Autowired
    public void process(@Value("${spring.kafka.topic.name}") String topicName,
                        StreamsBuilder builder) {
        Serde<String> keySerde = Serdes.String();
        Serde<String> valueSerde = Serdes.String();

        var stream = builder.stream(topicName, Consumed.with(keySerde, valueSerde));
        stream.foreach((key, value) -> {
            var keyValue = ImmutablePair.of(key, value);
            queue.offer(keyValue);
            log.info("key -> value: {}", keyValue);
        });
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

        public int size() {
            return queue.size();
        }
    }

}
