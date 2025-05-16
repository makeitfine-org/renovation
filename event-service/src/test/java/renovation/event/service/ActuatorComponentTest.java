package renovation.event.service;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import renovation.event.service.web.controller.base.KafkaTestcontainersConfigs;

import static io.restassured.RestAssured.given;

@Tag("componentTest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@ContextConfiguration(classes = KafkaTestcontainersConfigs.class)
class ActuatorComponentTest {

    @LocalServerPort
    protected Integer port;

    protected RequestSpecification request;

    @BeforeEach
    void init() {
        RestAssured.port = port;
        this.request = given().header("Content-Type", "application/json");
    }

    @Test
    void when_applicationStartedAllAreUp_Success() {
        request.get("/actuator/health")
                .then()
                .statusCode(HttpStatus.SC_OK)
                // rcon-monitor
                .body("status", CoreMatchers.equalTo("UP"))
                // kafka
                .body("components.kafka.status", CoreMatchers.equalTo("UP"))
                .body("components.kafka.details.bootstrapServers", CoreMatchers.startsWith("localhost:"))
                // schema-registry
                .body("components.schemaRegistry.status", CoreMatchers.equalTo("UP"))
                .body("components.schemaRegistry.details.url", CoreMatchers.startsWith("http://localhost:"))
                // disc
                .body("components.diskSpace.status", CoreMatchers.equalTo("UP"))
                .body("components.diskSpace.details.exists", CoreMatchers.equalTo(true))
                // common
                .body("components.livenessState.status", CoreMatchers.equalTo("UP"))
                .body("components.readinessState.status", CoreMatchers.equalTo("UP"))
                .body("components.ping.status", CoreMatchers.equalTo("UP"))
                // ssl
                .body("components.ssl.status", CoreMatchers.equalTo("UP"));
    }
}
