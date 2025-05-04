package renovation.event.service.config.actuator;

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SchemaRegistryHealthIndicator implements HealthIndicator {

    public static final int CACHE_CAPACITY = 100;

    private final SchemaRegistryClient schemaRegistryClient;

    private final String schemaRegistryUrl;

    public SchemaRegistryHealthIndicator(@Value("${spring.kafka.schema.registry.url}") String schemaRegistryUrl) {
        this.schemaRegistryUrl = schemaRegistryUrl;
        this.schemaRegistryClient = new CachedSchemaRegistryClient(schemaRegistryUrl, CACHE_CAPACITY);
    }

    @Override
    public Health health() {
        try {
            // Simple ping operation to check connectivity
            schemaRegistryClient.getAllSubjects();
            return Health.up()
                    .withDetail("status", "Schema Registry is available")
                    .withDetail("url", schemaRegistryUrl)
                    .build();
        } catch (IOException | RestClientException e) {
            return Health.down()
                    .withDetail("error", "Cannot connect to Schema Registry")
                    .withException(e)
                    .build();
        }
    }
}
