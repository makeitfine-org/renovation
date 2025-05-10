/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.event.service.service.streams;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import renovation.event.service.kafka.avro.record.work.WorkEvent;
import renovation.event.service.kafka.avro.record.work.WorkEventKey;
import renovation.event.service.kafka.avro.record.work.aggregate.WorkAggregateEvent;

@Slf4j
@Component
public class KTableProcessor {

    private final RestClient.Builder builder;
    @Value("${spring.kafka.streams.store.name}")
    private String storeName;

    @Value("${spring.kafka.schema.registry.url}")
    private String schemaRegistry;

    public KTableProcessor(RestClient.Builder builder) {
        this.builder = builder;
    }

    public void process(KStream<WorkEventKey, WorkEvent> stream) {

        KGroupedStream<String, Double> worksById = stream
                .map((key, work) -> new KeyValue<>(String.valueOf(work.getId()), work.getPrice()))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()));

        KTable<String, Long> workByIdCount = worksById.count(
                Materialized.<String, Long, KeyValueStore<org.apache.kafka.common.utils.Bytes, byte[]>>as("count-store")
                        .withLoggingDisabled()
        );
        workByIdCount.toStream().peek((k, v) -> {
            log.info("id -> count : {} -> {} ", k, v);
        }).to("count", Produced.with(Serdes.String(), Serdes.Long()));

        KTable<String, Double> workByIdTotalPrice = worksById.reduce(
                Double::sum,
                Materialized.<String, Double, KeyValueStore<Bytes, byte[]>>as("total-price-store")
                        .withLoggingDisabled()
                );
        workByIdTotalPrice.toStream().peek((k, v) -> {
            log.info("id -> total price : {} -> {} ", k, v);
        }).to("totalPrice", Produced.with(Serdes.String(), Serdes.Double()));

        KTable<String, WorkAggregateEvent> workByIdAggregate = worksById.aggregate(
                // Initializer
                () -> new WorkAggregateEvent(0, 0.0),
                // Aggregator
                (key, newPrice, aggregate) ->
                        new WorkAggregateEvent(
                                aggregate.getCount() + 1,
                                aggregate.getPriceSum() + newPrice
                        ),
                // Materialized with Serdes
                Materialized.<String, WorkAggregateEvent, KeyValueStore<org.apache.kafka.common.utils.Bytes, byte[]>>
                                as("avro-agg-store")
                        .withLoggingDisabled()
        );

        workByIdAggregate.toStream().peek((k, v) ->
                log.info("AGG: id={} => count={}, sum={}", k, v.getCount(), v.getPriceSum())
        ).to("avroTotalPrice");
    }
}
