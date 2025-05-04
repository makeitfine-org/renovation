package renovation.event.service.config.actuator;

import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Component
public class KafkaHealthIndicator implements HealthIndicator {

    public static final int TIMEOUT = 5_000;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Override
    public Health health() {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("request.timeout.ms", TIMEOUT);

        try (AdminClient client = AdminClient.create(props)) {
            client.listTopics().names().get(TIMEOUT, TimeUnit.MILLISECONDS);
            return Health.up()
                    .withDetail("status", "Kafka is available")
                    .withDetail("bootstrapServers", bootstrapServers)
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("bootstrapServers", bootstrapServers)
                    .withException(e)
                    .build();
        }
    }
}
