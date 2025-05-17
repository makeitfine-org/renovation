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
import org.springframework.test.context.ContextConfiguration;
import renovation.event.service.web.Route;
import renovation.event.service.web.controller.base.KafkaTestcontainersConfigs;
import renovation.event.service.web.controller.base.RestTestInit;

import static renovation.common.util.JsonUtil.simplify;

@Tag("componentTest")
@ContextConfiguration(classes = KafkaTestcontainersConfigs.class)
class StartupControllerTest extends RestTestInit {

    public StartupControllerTest() {
        super(Route.STARTUP);
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
