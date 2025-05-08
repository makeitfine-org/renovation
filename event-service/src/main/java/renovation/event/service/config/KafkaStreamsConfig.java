/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.event.service.config;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import renovation.event.service.kafka.avro.record.work.WorkEvent;
import renovation.event.service.kafka.avro.record.work.WorkEventKey;

@Configuration
@EnableKafkaStreams
public class KafkaStreamsConfig {

    @Bean
    public KStream<WorkEventKey, WorkEvent> kStream(
            StreamsBuilder kStreamBuilder,
            @Value("${spring.kafka.topic.event.name}") String inputTopic
    ) {
        KStream<WorkEventKey, WorkEvent> stream = kStreamBuilder.stream(inputTopic);

        return stream;
    }
}
