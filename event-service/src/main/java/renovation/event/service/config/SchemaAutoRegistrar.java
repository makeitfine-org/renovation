/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */
package renovation.event.service.config;

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class SchemaAutoRegistrar {
    public static final int CACHE_CAPACITY = 100;

    @Value("${spring.kafka.schema.registry.url}")
    private String schemaRegistryUrl;

    @Value("${spring.kafka.schema.avro.location}")
    private String avroLocationInResources;

    @PostConstruct
    public void registerAllSchemas() throws Exception {
        SchemaRegistryClient client = new CachedSchemaRegistryClient(schemaRegistryUrl, CACHE_CAPACITY);

        List<Path> schemaPaths = findAllAvscFiles(avroLocationInResources);
        for (Path path : schemaPaths) {
            String schemaStr = Files.readString(path);
            Schema avroSchema = new Schema.Parser().parse(schemaStr);

            // Subject = namespace.name
            String subject = avroSchema.getFullName();

            int id = client.register(subject, avroSchema);
            log.info("✅ Registered {} as ID: {}", subject, id);
        }
    }

    private List<Path> findAllAvscFiles(String base) throws Exception {
        URI uri = Objects.requireNonNull(getClass().getClassLoader().getResource(base)).toURI();
        Path root = Paths.get(uri);

        try (Stream<Path> files = Files.walk(root)) {
            return files.filter(p -> p.toString().endsWith(".avsc")).collect(Collectors.toList());
        }
    }
}
