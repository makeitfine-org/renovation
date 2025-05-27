/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.influx.service.web.controller;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import renovation.common.web.RestTestInit;
import renovation.influx.service.base.InfluxTestcontainers;
import renovation.influx.service.web.Route;

import java.util.concurrent.TimeUnit;

@Tag("componentTest")
@ContextConfiguration(classes = InfluxTestcontainers.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TemperatureControllerTest extends RestTestInit {

    public TemperatureControllerTest() {
        super(Route.TEMPERATURE);
    }

    @Test
    void when_WriteAndRead_expect_Success() throws InterruptedException {
        // when
        write("hall", 23.5);
        write("hall", 24);

        write("bedroom", 24.3);
        write("bedroom", 24);
        write("bedroom", 24.1);

        // then
        getRequestNew()
                .queryParam("location", "hall")
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("size()", Matchers.equalTo(2))
                .body("[0].location", Matchers.equalTo("hall"))
                .body("[0].value", Matchers.equalTo(23.5f))
                .body("[1].location", Matchers.equalTo("hall"))
                .body("[1].value", Matchers.equalTo(24.0f));

        getRequestNew()
                .queryParam("location", "bedroom")
                .get()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("size()", Matchers.equalTo(3))
                .body("[0].location", Matchers.equalTo("bedroom"))
                .body("[0].value", Matchers.equalTo(24.3f))
                .body("[1].location", Matchers.equalTo("bedroom"))
                .body("[1].value", Matchers.equalTo(24.0f))
                .body("[2].location", Matchers.equalTo("bedroom"))
                .body("[2].value", Matchers.equalTo(24.1f));
    }

    private void write(String location, double value) throws InterruptedException {
        getRequestNew()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .queryParam("location", location)
                .queryParam("value", value)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);

        TimeUnit.MILLISECONDS.sleep(350);
    }
}
