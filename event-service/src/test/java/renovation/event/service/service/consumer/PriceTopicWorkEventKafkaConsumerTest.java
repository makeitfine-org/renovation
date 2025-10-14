/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.event.service.service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import renovation.event.service.service.mapper.WorkEventMapper;
import renovation.event.service.service.producer.WorkEventKafkaProducer;
import renovation.event.service.web.controller.base.KafkaTestcontainersConfigs;
import renovation.event.service.web.dto.WorkEventRequest;

import static renovation.common.util.MapperUtil.OBJECT_MAPPER;
import static renovation.common.util.MapperUtil.jsonFileContentFromSrcTestResources;

@Tag("integrationTest")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@ContextConfiguration(classes = KafkaTestcontainersConfigs.class)
class PriceTopicWorkEventKafkaConsumerTest {

    private final WorkEventMapper workEventMapper;
    private final WorkEventKafkaProducer workEventProducer;
    private final String topicName;
    private final PriceTopicWorkEventKafkaConsumer consumer;

    public PriceTopicWorkEventKafkaConsumerTest(
            @Autowired WorkEventMapper workEventMapper,
            @Autowired WorkEventKafkaProducer workEventProducer,
            @Value("${spring.kafka.topic.price.name}") String topicName,
            @Autowired PriceTopicWorkEventKafkaConsumer consumer
    ) {
        this.workEventMapper = workEventMapper;
        this.workEventProducer = workEventProducer;
        this.topicName = topicName;
        this.consumer = consumer;
    }

    @Test
    void kafkaListener() throws InterruptedException, JsonProcessingException {
        var body = jsonFileContentFromSrcTestResources(
                "KafkaControllerComponentTest.when_publish_expect_Success.json"
        );
        var request = OBJECT_MAPPER.readValue(body, WorkEventRequest.class);

        var key = workEventMapper.toAvroWorkEventKey(null);
        var data = workEventMapper.toAvroWorkEvent(request);
        workEventProducer.send(key, data, topicName);

        var keyValue = consumer.pollLastKeyValue();
        Assertions.assertEquals(key, keyValue.getKey());
        Assertions.assertEquals(data, keyValue.getValue());
    }
}
