/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.event.service.service.streams;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import renovation.event.service.web.Route;
import renovation.event.service.web.controller.base.KafkaTestcontainersConfigs;
import renovation.event.service.web.controller.base.RestTestInit;

import java.util.concurrent.TimeUnit;

@Tag("componentTest")
@ContextConfiguration(classes = KafkaTestcontainersConfigs.class)
class StreamProcessorTest extends RestTestInit {

    private StreamProcessor streamProcessor;

    public StreamProcessorTest(@Autowired StreamProcessor streamProcessor) {
        super(Route.KAFKA);
        this.streamProcessor = streamProcessor;
    }

    @Test
    void process() throws InterruptedException {
        var body = jsonFileContentFromSrcTestResources(
                "KafkaControllerComponentTest.when_publish_expect_Success.json"
        );

        postRequest("/publish", body, HttpStatus.SC_OK);

        var keyValue = streamProcessor.getQueue().poll(5, TimeUnit.SECONDS);

        Assertions.assertNotNull(keyValue.getKey());
        Assertions.assertEquals(
                "\u0000\u0000\u0000\u0000\u0002\u00021\u000E" +
                        "title 1\f" +
                        "desc 1" +
                        "\u00142023-11-27\u0000\u0000\u0000\u0000\u00000n@\u00142023-11-25",
                keyValue.getValue().toString()
        );
    }
}
