/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.event.service.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.TopicBuilder;
import renovation.event.service.kafka.avro.record.work.WorkEvent;
import renovation.event.service.kafka.avro.record.work.WorkEventKey;
import renovation.event.service.service.streams.KStreamProcessor;
import renovation.event.service.service.streams.KTableProcessor;

@Configuration
@EnableKafkaStreams
@Slf4j
public class KafkaStreamsConfig {

    @Bean
    public NewTopic createPriceTopic(@Value("${spring.kafka.topic.price.name}") String topicName) {
        return TopicBuilder.name(topicName)
                .compact()
                .build();
    }

    private final KStreamProcessor kStreamProcessor;

    private final KTableProcessor kTableProcessor;

    public KafkaStreamsConfig(KStreamProcessor kStreamProcessor, KTableProcessor kTableProcessor) {
        this.kStreamProcessor = kStreamProcessor;
        this.kTableProcessor = kTableProcessor;
    }

    @Bean
    public KStream<WorkEventKey, WorkEvent> kStream(
            StreamsBuilder kStreamBuilder,
            @Value("${spring.kafka.topic.event.name}") String inputTopic
    ) {
        KStream<WorkEventKey, WorkEvent> stream = kStreamBuilder.stream(inputTopic);

        kStreamProcessor.process(stream);

        kTableProcessor.process(stream); // todo: uncomment

        return stream;
    }
}
