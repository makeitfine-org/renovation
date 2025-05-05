/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.event.service.web.controller.base;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

@TestConfiguration(proxyBeanMethods = false)
public class KafkaTestcontainersInit {
    public static final String KAFKA_IMAGE = "confluentinc/cp-kafka";
    public static final String SCHEMA_REGISTRY_IMAGE = "confluentinc/cp-schema-registry";
    public static final String KAFKA_VERSION = "7.8.0";

    private final Network NETWORK = Network.newNetwork();

    @Bean
    @ServiceConnection(name = "kafka")
    ConfluentKafkaContainer kafka() {
        return new ConfluentKafkaContainer(DockerImageName.parse(KAFKA_IMAGE).withTag(KAFKA_VERSION))
                .withNetwork(NETWORK);
    }

    @Bean
    GenericContainer<?> schema(ConfluentKafkaContainer kafka) {
        return new GenericContainer<>(DockerImageName.parse(SCHEMA_REGISTRY_IMAGE).withTag(KAFKA_VERSION))
                .withNetwork(NETWORK)
                .withExposedPorts(8081)
                .withEnv("SCHEMA_REGISTRY_HOST_NAME", "schema-registry")
                .withEnv("SCHEMA_REGISTRY_LISTENERS", "http://0.0.0.0:8081")
                .withEnv(
                        "SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS",
                        "PLAINTEXT://" + kafka.getNetworkAliases().get(0) + ":9093"
                )
                .withEnv("SCHEMA_REGISTRY_KAFKASTORE_SECURITY_PROTOCOL", "PLAINTEXT")
                .waitingFor(Wait.forHttp("/subjects").forStatusCode(200))
                .withStartupTimeout(Duration.ofSeconds(60));
    }

    @Bean
    DynamicPropertyRegistrar kafkaProperties(ConfluentKafkaContainer kafka, GenericContainer<?> schema) {
        return registry -> {
            registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
            registry.add("spring.kafka.schema.registry.url",
                    () -> "http://" + schema.getHost() + ":" + schema.getMappedPort(8081));
        };
    }
}
