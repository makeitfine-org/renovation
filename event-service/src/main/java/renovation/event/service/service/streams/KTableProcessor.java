/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.event.service.service.streams;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import renovation.event.service.kafka.avro.record.work.WorkEvent;
import renovation.event.service.kafka.avro.record.work.WorkEventKey;
import renovation.event.service.kafka.avro.record.work.aggregate.WorkAggregateEvent;

import java.util.Collections;
import java.util.Map;

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
        //Create a new KeyValue Store
//        KeyValueBytesStoreSupplier worksByIdPriceStore = Stores.persistentKeyValueStore(storeName);

//        KGroupedStream<WorkEventKey, WorkEvent> worksById = stream.groupByKey();
//
//        KTable<WorkEventKey, Long> workByIdCount = worksById.count();

        KGroupedStream<String, Double> worksById = stream
                .map((key, work) -> new KeyValue<>(String.valueOf(work.getId()), work.getPrice()))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Double()));

        KTable<String, Long> workByIdCount = worksById.count();
        workByIdCount.toStream().foreach((k, v) -> {
            log.info("id -> count : {} -> {} ", k, v);
        });

        KTable<String, Double> workByIdTotalPrice = worksById.reduce(
                Double::sum
        );
        workByIdTotalPrice.toStream().foreach((k, v) -> {
            log.info("id -> total price : {} -> {} ", k, v);
        });

        Map<String, String> serdeConfig = Collections.singletonMap("schema.registry.url", schemaRegistry);
        SpecificAvroSerde<WorkAggregateEvent> aggregateSerde = new SpecificAvroSerde<>();
        aggregateSerde.configure(serdeConfig, false);
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
                Materialized.with(Serdes.String(), aggregateSerde)
        );


//        KGroupedStream<String, Double> worksById = stream
//                .map((key, work) -> new KeyValue(work.getId().toString(), work.getPrice()))
//                .groupByKey();

//        log.info(">>> {}", worksById.count());

//        final Map<String, String> serdeConfig = Collections.singletonMap("schema.registry.url", schemaRegistry);
//        final SpecificAvroSerde<WorkAggregateEvent> valueSpecificAvroSerde = new SpecificAvroSerde<>();
//        valueSpecificAvroSerde.configure(serdeConfig, false);


//        KTable<String, WorkAggregateEvent> workAggregate = worksById.aggregate(
//                () -> WorkAggregateEvent.newBuilder().setCount(0).setPriceSum(0.0).build(),
//                (key, value, aggregate) -> WorkAggregateEvent.newBuilder()
//                        .setCount(aggregate.getCount() + 1)
//                        .setPriceSum(aggregate.getPriceSum() + value)
//                        .build(),
//                Materialized.with(Serdes.String(), valueSpecificAvroSerde)
//        );

//        KTable<String, WorkAggregateEvent> workAggregate = worksById.aggregate(
//                () -> WorkAggregateEvent.newBuilder().setCount(0).setPriceSum(0.0).build(),
//                (key, value, aggregate) -> {
//                    if (aggregate == null) {
//                        return WorkAggregateEvent.newBuilder()
//                                .setCount(1)
//                                .setPriceSum(value)
//                                .build();
//                    } else {
//                        return WorkAggregateEvent.newBuilder()
//                                .setCount(aggregate.getCount() + 1)
//                                .setPriceSum(aggregate.getPriceSum() + value)
//                                .build();
//                    }
//                },
//                Materialized.with(Serdes.String(), valueSpecificAvroSerde)
//        );

//        final KTable<String, Double> workTotal =
//                workAggregate.mapValues(
//                        value -> value.getPriceSum(),
//                        Materialized.as(worksByIdPriceStore)
//                );
//
//        final KTable<String, Double> workTotal = workAggregate.mapValues(
//                WorkAggregateEvent::getPriceSum,
//                Materialized.<String, Double>as(worksByIdPriceStore)
//                        .withKeySerde(Serdes.String())
//                        .withValueSerde(Serdes.Double())
//        );
    }
}
