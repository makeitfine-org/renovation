package renovation.event.service.web.controller.base;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static renovation.event.service.util.Helper.readFileContentFromProjectRoot;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class RestTestInit {

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

    protected String jsonFileContentFromSrcTestResources(String pathInSrcTestResources) {
        return readFileContentFromProjectRoot("src/test/resources/json/" + pathInSrcTestResources);
    }

    protected void postRequest(String relatedPath, String body, int expectedCode) {
        request
                .body(body)
                .when()
                .post(relatedPath)
                .then()
                .statusCode(expectedCode);
    }
}
