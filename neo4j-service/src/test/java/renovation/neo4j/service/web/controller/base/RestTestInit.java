package renovation.neo4j.service.web.controller.base;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class RestTestInit { //todo: refactoring/move to common module

    @LocalServerPort
    protected Integer port;

    protected RequestSpecification request;

    private String basePath;

    public RestTestInit(String basePath) {
        this.basePath = basePath;
    }

    @BeforeEach
    protected void init() {
        RestAssured.port = port;
        this.request = given()
                .header("Content-Type", "application/json")
                .basePath(basePath);
    }
}
