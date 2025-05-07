/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.event.service.service.streams;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
@EnableKafkaStreams
public class KafkaStreamProcessorConfig {

    @Bean
    KafkaStreamsConfiguration defaultKafkaStreamsConfig(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrap,
            @Value("${spring.kafka.schema.registry.url}") String schemaRegistry,
            @Value("${spring.application.name}") String appId
    ) {
        Map<String, Object> configs = new HashMap<>();
        configs.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        configs.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry);
        configs.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
//        configs.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class);
        configs.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
        configs.put(StreamsConfig.APPLICATION_ID_CONFIG, appId);
//        configs.put(JsonDeserializer.VALUE_DEFAULT_TYPE, JsonSerializer.class);
//        configs.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Serdes.String().getClass());
        configs.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return new KafkaStreamsConfiguration(configs);
    }
}
