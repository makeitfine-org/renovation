/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.event.service.web.controller;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import renovation.event.service.web.Route;

import static io.restassured.RestAssured.given;
import static renovation.event.service.TestUtil.simplify;

@Tag("integrationTest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StartupControllerTest {

    private Integer port;
    private RequestSpecification request;

    public StartupControllerTest(@LocalServerPort Integer port) {
        this.port = port;
    }

    @BeforeEach
    void init() {
        RestAssured.port = port;
        this.request = given()
                .header("Content-Type", "application/json")
                .basePath(Route.STARTUP);
    }

    @Test
    void about() {
        request
                .when()
                .get("/about")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(CoreMatchers.equalTo(
                        simplify(
                                """
                                        {
                                            "name"        : "event-service",
                                            "description" : "Event service module"
                                        }
                                        """)
                ));
    }

    @Test
    void index() {
        request
                .when()
                .get("/module")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(CoreMatchers.equalTo("Hi, it's \"Event-service\" module"));
    }
}
