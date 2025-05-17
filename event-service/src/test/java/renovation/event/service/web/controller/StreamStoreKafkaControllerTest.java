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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import renovation.event.service.web.Route;
import renovation.event.service.web.controller.base.KafkaTestcontainersConfigs;
import renovation.event.service.web.controller.base.RestTestInit;

import java.util.concurrent.TimeUnit;

import static renovation.common.util.JsonUtil.simplify;
import static renovation.common.util.MapperUtil.jsonFileContentFromSrcTestResources;
import static renovation.event.service.KafkaUtil.STREAMS_INIT_TIME_WAIT;

@Tag("componentTest")
//https://docs.spring.io/spring-framework/reference/testing/annotations/integration-spring/annotation-dirtiescontext.html
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@ContextConfiguration(classes = KafkaTestcontainersConfigs.class)
class StreamStoreKafkaControllerTest extends RestTestInit {

    public StreamStoreKafkaControllerTest() {
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
