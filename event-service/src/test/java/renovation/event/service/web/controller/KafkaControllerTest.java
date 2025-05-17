/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.event.service.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import renovation.event.service.service.consumer.EventTopicWorkEventKafkaConsumer;
import renovation.event.service.service.consumer.PriceTopicWorkEventKafkaConsumer;
import renovation.event.service.service.mapper.WorkEventRequestMapper;
import renovation.event.service.web.Route;
import renovation.event.service.web.controller.base.KafkaTestcontainersConfigs;
import renovation.event.service.web.controller.base.RestTestInit;
import renovation.event.service.web.dto.WorkEventRequest;

import java.util.UUID;

import static renovation.event.service.TestUtil.jsonFileContentFromSrcTestResources;
import static renovation.event.service.util.Helper.OBJECT_MAPPER;

@Tag("componentTest")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@ContextConfiguration(classes = KafkaTestcontainersConfigs.class)
class KafkaControllerTest extends RestTestInit {

    private final EventTopicWorkEventKafkaConsumer eventConsumer;
    private final PriceTopicWorkEventKafkaConsumer priceConsumer;
    private final WorkEventRequestMapper workEventRequestMapper;

    public KafkaControllerTest(
            @Autowired EventTopicWorkEventKafkaConsumer eventConsumer,
            @Autowired PriceTopicWorkEventKafkaConsumer priceConsumer,
            @Autowired WorkEventRequestMapper workEventRequestMapper
    ) {
        super(Route.KAFKA);
        this.eventConsumer = eventConsumer;
        this.priceConsumer = priceConsumer;
        this.workEventRequestMapper = workEventRequestMapper;
    }

    @Disabled //todo: working locally/fix for github actions
    @Test
    void when_publish_expect_Success() throws InterruptedException, JsonProcessingException {
        var body = jsonFileContentFromSrcTestResources(
                "KafkaControllerComponentTest.when_publish_expect_Success.json"
        );

        postRequest("/publish", body, HttpStatus.SC_OK);

        var workEventKeyValue = eventConsumer.pollLastKeyValue();
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

    @Disabled //todo: working locally/fix for github actions
    @Test
    void when_publish_price_expect_Success() throws InterruptedException, JsonProcessingException {
        var body = jsonFileContentFromSrcTestResources(
                "KafkaControllerComponentTest.when_publish_expect_Success.json"
        );

        postRequest("/publish/price", body, HttpStatus.SC_OK);

        var workEventKeyValue = priceConsumer.pollLastKeyValue();
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
}
