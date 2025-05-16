/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.event.service.service.streams;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import renovation.event.service.service.mapper.WorkEventMapper;
import renovation.event.service.service.producer.WorkEventKafkaProducer;
import renovation.event.service.web.controller.base.KafkaTestcontainersConfigs;
import renovation.event.service.web.dto.WorkEventRequest;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static renovation.event.service.TestUtil.jsonFileContentFromSrcTestResources;
import static renovation.event.service.util.Helper.OBJECT_MAPPER;

@Tag("componentTest")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@ContextConfiguration(classes = KafkaTestcontainersConfigs.class)
class StreamProcessorTest {

    private final WorkEventMapper workEventMapper;
    private final WorkEventKafkaProducer workEventProducer;
    private final StreamProcessor streamProcessor;

    public StreamProcessorTest(
            @Autowired WorkEventMapper workEventMapper,
            @Autowired WorkEventKafkaProducer workEventProducer,
            @Autowired StreamProcessor streamProcessor
    ) {
        this.workEventMapper = workEventMapper;
        this.workEventProducer = workEventProducer;
        this.streamProcessor = streamProcessor;
    }

    @Test
    void process() throws InterruptedException, JsonProcessingException {
        var body = jsonFileContentFromSrcTestResources(
                "KafkaControllerComponentTest.when_publish_expect_Success.json"
        );
        var request = OBJECT_MAPPER.readValue(body, WorkEventRequest.class);

        var key = workEventMapper.toAvroWorkEventKey(null);
        var data = workEventMapper.toAvroWorkEvent(request);
        workEventProducer.send(key, data);

        var keyValue = streamProcessor.getQueue().poll(5, TimeUnit.SECONDS);
        Assertions.assertEquals(UUID.fromString(key.getId().toString()), keyValue.getKey());
        Assertions.assertEquals(request, keyValue.getValue());
    }
}
