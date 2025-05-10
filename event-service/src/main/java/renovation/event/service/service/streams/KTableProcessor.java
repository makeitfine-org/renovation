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
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.Stores;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import renovation.event.service.kafka.avro.record.work.WorkEvent;
import renovation.event.service.kafka.avro.record.work.WorkEventKey;
import renovation.event.service.kafka.avro.record.work.aggregate.WorkAggregateEvent;

@Slf4j
@Component
public class KTableProcessor {
    private String storeName;

    public KTableProcessor(@Value("${spring.kafka.streams.store.name}") String storeName) {
        this.storeName = storeName;
    }

    public void process(KStream<WorkEventKey, WorkEvent> stream) {
        KGroupedStream<String, Double> priceByWorkIdGroupedStream = stream
                .map((key, work) -> new KeyValue<>(String.valueOf(work.getId()), work.getPrice()))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()));

        KTable<String, Long> workCountByIdTable = priceByWorkIdGroupedStream.count(
                Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("work-count-store")
                        .withLoggingDisabled()
        );

        workCountByIdTable.toStream()
                .peek((key, count) -> log.info("work-id -> count: {} -> {}", key, count))
                .to("work-count-topic", Produced.with(Serdes.String(), Serdes.Long()));

        KTable<String, Double> totalPriceByWorkIdTable = priceByWorkIdGroupedStream.reduce(
                Double::sum,
                Materialized.<String, Double, KeyValueStore<Bytes, byte[]>>as("work-total-price-store")
                        .withLoggingDisabled()
        );

        totalPriceByWorkIdTable.toStream()
                .peek((key, total) -> log.info("work-id -> total price: {} -> {}", key, total))
                .to("work-total-price-topic", Produced.with(Serdes.String(), Serdes.Double()));

        KTable<String, WorkAggregateEvent> workAggregateByIdTable = priceByWorkIdGroupedStream.aggregate(
                () -> new WorkAggregateEvent(0, 0.0),
                (key, newPrice, aggregate) ->
                        new WorkAggregateEvent(
                                aggregate.getCount() + 1,
                                aggregate.getPriceSum() + newPrice
                        ),
                Materialized.<String, WorkAggregateEvent, KeyValueStore<Bytes, byte[]>>
                                as("work-aggregate-avro-store")
                        .withLoggingDisabled()
        );

        workAggregateByIdTable.toStream()
                .peek((key, aggregate) ->
                        log.info("AGG: work-id={} => count={}, sum={}",
                                key, aggregate.getCount(), aggregate.getPriceSum()))
                .to("work-aggregate-avro-topic");

        // Fork #2 — Map to priceSum and store it in a different state store
        KeyValueBytesStoreSupplier priceSumStore = Stores.persistentKeyValueStore(storeName);

        workAggregateByIdTable.mapValues(
                WorkAggregateEvent::getPriceSum,
                Materialized.<String, Double>as(priceSumStore)
                        .withKeySerde(Serdes.String())
                        .withValueSerde(Serdes.Double()));
    }
}
