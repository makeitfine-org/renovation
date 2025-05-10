/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.event.service.service.streams;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import renovation.event.service.kafka.avro.record.work.WorkEvent;
import renovation.event.service.kafka.avro.record.work.WorkEventKey;
import renovation.event.service.service.streams.util.ComparisonUtil;

@Slf4j
@Component
public class KStreamProcessor {
    @Value("${spring.kafka.topic.price.name}")
    private String outputTopic;

    @Value("${spring.kafka.topic.price.comparison}")
    private String comparison;

    @Value("${spring.kafka.topic.price.number}")
    private double priceNumber;

    private ComparisonUtil comparisonUtil;

    @PostConstruct
    public void init() {
        this.comparisonUtil = new ComparisonUtil(comparison, priceNumber);
    }

    public void process(KStream<WorkEventKey, WorkEvent> stream) {
        stream
                .peek((k, v) -> log.debug("before filtering: {}", v))
                .filter(
                (key, value) -> value != null && comparison(value)
        ).to(outputTopic);
    }

    private boolean comparison(WorkEvent workEvent) {
        return comparisonUtil.compare(workEvent.getPrice());
    }
}
