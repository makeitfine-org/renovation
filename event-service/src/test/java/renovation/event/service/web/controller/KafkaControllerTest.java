/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.event.service.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import renovation.event.service.service.consumer.WorkEventKafkaConsumer;
import renovation.event.service.service.mapper.WorkEventRequestMapper;
import renovation.event.service.web.Route;
import renovation.event.service.web.controller.base.KafkaTestcontainersInit;
import renovation.event.service.web.controller.base.RestTestInit;
import renovation.event.service.web.dto.WorkEventRequest;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static renovation.event.service.util.Helper.OBJECT_MAPPER;

@Tag("componentTest")
@ContextConfiguration(classes = KafkaTestcontainersInit.class)
class KafkaControllerTest extends RestTestInit {

    public KafkaControllerTest() {
        super(Route.KAFKA);
    }

    @Autowired
    private WorkEventKafkaConsumer consumer;

    @Autowired
    private WorkEventRequestMapper workEventRequestMapper;

    @Test
    void when_publish_expect_Success() throws InterruptedException, JsonProcessingException {
        // assert last saved record is null
        Assertions.assertNull(consumer.getLastWorkEventKeyValue());

        var body = jsonFileContentFromSrcTestResources(
                "KafkaControllerComponentTest.when_publish_expect_Success.json"
        );

        request
                .body(body)
                .when()
                .post("/publish")
                .then()
                .statusCode(HttpStatus.SC_OK);

        waitForSleepConsumption();

        var workEventKeyValue = consumer.getLastWorkEventKeyValue();
        Assertions.assertNotNull(
                UUID.fromString(
                        String.valueOf(workEventKeyValue.getKey().getId())
                )
        );
        Assertions.assertNotNull(workEventKeyValue.getValue());
        Assertions.assertEquals(
                OBJECT_MAPPER.readValue(body, WorkEventRequest.class),
                workEventRequestMapper.toWorkEventRequest(
                        workEventKeyValue.getValue()
                )
        );
    }

    private void waitForSleepConsumption() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(500); // todo: think of decreasing (future task)
    }
}
