/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.event.service.web.controller;

import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;
import renovation.event.service.web.Route;
import renovation.event.service.web.controller.base.RestTestInit;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static renovation.common.util.JsonUtil.simplify;
import static renovation.common.util.MapperUtil.jsonFileContentFromSrcTestResources;
import static renovation.event.service.KafkaUtil.STREAMS_INIT_TIME_WAIT;

@Tag("integrationTest")
@Testcontainers
class TestcontainersStreamStoreKafkaControllerTest extends RestTestInit {

    public static final String KAFKA_IMAGE = "confluentinc/cp-kafka";
    public static final String SCHEMA_REGISTRY_IMAGE = "confluentinc/cp-schema-registry";
    public static final String KAFKA_VERSION = "7.8.0";

    private static final Network NETWORK = Network.newNetwork();

    @Container
    static final ConfluentKafkaContainer KAFKA_CONTAINER =
            new ConfluentKafkaContainer(DockerImageName.parse(KAFKA_IMAGE).withTag(KAFKA_VERSION))
                    .withNetwork(NETWORK);

    @Container
    static final GenericContainer<?> SCHEMA_REGISTRY =
            new GenericContainer<>(DockerImageName.parse(SCHEMA_REGISTRY_IMAGE).withTag(KAFKA_VERSION))
                    .withNetwork(NETWORK)
                    .withExposedPorts(8081)
                    .withEnv("SCHEMA_REGISTRY_HOST_NAME", "schema-registry")
                    .withEnv("SCHEMA_REGISTRY_LISTENERS", "http://0.0.0.0:8081")
                    .withEnv(
                            "SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS",
                            "PLAINTEXT://" + KAFKA_CONTAINER.getNetworkAliases().get(0) + ":9093"
                    )
                    .withEnv("SCHEMA_REGISTRY_KAFKASTORE_SECURITY_PROTOCOL", "PLAINTEXT")
                    .waitingFor(Wait.forHttp("/subjects").forStatusCode(200))
                    .withStartupTimeout(Duration.ofSeconds(60));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
        registry.add("spring.kafka.schema.registry.url",
                () -> "http://" + SCHEMA_REGISTRY.getHost() + ":" + SCHEMA_REGISTRY.getMappedPort(8081));
    }

    public TestcontainersStreamStoreKafkaControllerTest() {
        super(Route.KAFKA);
    }

    @Test
    void when_work_id_expect_Success() throws InterruptedException {
        // When
        var body = jsonFileContentFromSrcTestResources(
                "KafkaControllerComponentTest.when_publish_id_1_expect_Success.json"
        );
        postRequest("/publish", body, HttpStatus.SC_OK);
        postRequest("/publish", body, HttpStatus.SC_OK);

        body = jsonFileContentFromSrcTestResources(
                "KafkaControllerComponentTest.when_publish_id_2_expect_Success.json"
        );
        postRequest("/publish", body, HttpStatus.SC_OK);

        body = jsonFileContentFromSrcTestResources(
                "KafkaControllerComponentTest.when_publish_id_3_expect_Success.json"
        );
        postRequest("/publish", body, HttpStatus.SC_OK);
        postRequest("/publish", body, HttpStatus.SC_OK);
        postRequest("/publish", body, HttpStatus.SC_OK);

        // wait until streams executes
        TimeUnit.SECONDS.sleep(STREAMS_INIT_TIME_WAIT);

        // Then
        request
                .when()
                .get("/work/1")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(CoreMatchers.equalTo(
                        simplify(
                                """
                                        {
                                            "workId"   : "1",
                                            "priceSum" : -154.2
                                        }
                                        """)
                ));
        request
                .when()
                .get("/work/2")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(CoreMatchers.equalTo(
                        simplify(
                                """
                                        {
                                            "workId"   : "2",
                                            "priceSum" : 133.55
                                        }
                                        """)
                ));
        request
                .when()
                .get("/work/3")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(CoreMatchers.equalTo(
                        simplify(
                                """
                                        {
                                            "workId"   : "3",
                                            "priceSum" : 3.0
                                        }
                                        """)
                ));

        // not exists
        request
                .when()
                .get("/work/1515223")
                .then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}
