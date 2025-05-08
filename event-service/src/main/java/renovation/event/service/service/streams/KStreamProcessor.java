/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.event.service.service.streams;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import renovation.event.service.kafka.avro.record.work.WorkEvent;
import renovation.event.service.kafka.avro.record.work.WorkEventKey;
import renovation.event.service.service.streams.util.ComparisonUtil;

@Component
public class KStreamProcessor {
    @Value("${spring.kafka.topic.price.name}")
    private String outputTopic;

    @Value("${spring.kafka.topic.price.comparison}")
    private String comparison;

    @Value("${spring.kafka.topic.price.number}")
    private double priceNumber;

    private final ComparisonUtil comparisonUtil;

    public KStreamProcessor() {
        this.comparisonUtil = new ComparisonUtil(comparison, priceNumber);
    }

    public void process(KStream<WorkEventKey, WorkEvent> stream) {
        stream.filter(
                (key, value) -> value != null && comparison(value)
        ).to(outputTopic);
    }

    private boolean comparison(WorkEvent workEvent) {
        return comparison.equalsIgnoreCase("<") && comparisonUtil.compare(workEvent.getPrice());
    }
}
