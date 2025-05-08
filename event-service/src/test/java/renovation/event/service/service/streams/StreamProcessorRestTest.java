/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.event.service.service.streams;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import renovation.event.service.web.Route;
import renovation.event.service.web.controller.base.KafkaTestcontainersConfigs;
import renovation.event.service.web.controller.base.RestTestInit;
import renovation.event.service.web.dto.WorkEventRequest;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static renovation.event.service.TestUtil.jsonFileContentFromSrcTestResources;
import static renovation.event.service.util.Helper.OBJECT_MAPPER;

@Tag("componentTest")
@ContextConfiguration(classes = KafkaTestcontainersConfigs.class)
class StreamProcessorRestTest extends RestTestInit {

    private StreamProcessor streamProcessor;

    public StreamProcessorRestTest(@Autowired StreamProcessor streamProcessor) {
        super(Route.KAFKA);
        this.streamProcessor = streamProcessor;
    }

    @Test
    void process() throws InterruptedException, JsonProcessingException {
        var body = jsonFileContentFromSrcTestResources(
                "KafkaControllerComponentTest.when_publish_expect_Success.json"
        );

        postRequest("/publish", body, HttpStatus.SC_OK);

        var keyValue = streamProcessor.getQueue().poll(5, TimeUnit.SECONDS);

        Assertions.assertNotNull(
                UUID.fromString(
                        String.valueOf(keyValue.getKey())
                )
        );
        Assertions.assertEquals(
                OBJECT_MAPPER.readValue(body, WorkEventRequest.class),
                keyValue.getValue()
        );
    }
}
