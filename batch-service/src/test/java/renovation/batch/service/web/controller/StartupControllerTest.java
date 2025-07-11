/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.batch.service.web.controller;

import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import renovation.batch.service.configs.PostgresSQLContainerConfigs;
import renovation.batch.service.web.Route;
import renovation.common.web.RestTestInit;

import static renovation.common.util.JsonUtil.simplify;

@Tag("componentTest")
@ContextConfiguration(classes = PostgresSQLContainerConfigs.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StartupControllerTest extends RestTestInit {

    public StartupControllerTest() {
        super(Route.STARTUP);
    }

    @Test
    void about() {
        getRequest().get("/about")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(CoreMatchers.equalTo(
                        simplify(
                                """
                                        {
                                            "name"        : "batch-service",
                                            "description" : "Batch service module"
                                        }
                                        """)
                ));
    }

    @Test
    void index() {
        getRequest().get("/module")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(CoreMatchers.equalTo("Hi, it's \"Batch-service\" module"));
    }
}
