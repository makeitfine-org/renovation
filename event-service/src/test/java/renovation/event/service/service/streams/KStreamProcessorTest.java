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
import org.springframework.test.context.ContextConfiguration;
import renovation.event.service.TestUtil;
import renovation.event.service.service.consumer.PriceTopicWorkEventKafkaConsumer;
import renovation.event.service.service.mapper.WorkEventMapper;
import renovation.event.service.service.producer.WorkEventKafkaProducer;
import renovation.event.service.web.controller.base.KafkaTestcontainersConfigs;

@Tag("componentTest")
@SpringBootTest
@ContextConfiguration(classes = KafkaTestcontainersConfigs.class)
class KStreamProcessorTest {

    private final WorkEventMapper workEventMapper;
    private final WorkEventKafkaProducer workEventProducer;
    private final PriceTopicWorkEventKafkaConsumer priceConsumer;

    public KStreamProcessorTest(
            @Autowired WorkEventMapper workEventMapper,
            @Autowired WorkEventKafkaProducer workEventProducer,
            @Autowired PriceTopicWorkEventKafkaConsumer priceConsumer
    ) {
        this.workEventMapper = workEventMapper;
        this.workEventProducer = workEventProducer;
        this.priceConsumer = priceConsumer;
    }

    @Test
    void process() throws InterruptedException, JsonProcessingException {

        //send price higher
        var keyValueAvro = TestUtil.keyValueFromJsonFileContentFromSrcTestResources(
                workEventMapper,
                "KafkaControllerComponentTest.when_publish_expect_Success.json"
        );
        workEventProducer.send(keyValueAvro.getKey(), keyValueAvro.getValue());
        var keyValue = priceConsumer.pollLastKeyValue();
        Assertions.assertNull(keyValue);

        //send price lower
        keyValueAvro = TestUtil.keyValueFromJsonFileContentFromSrcTestResources(
                workEventMapper,
                "KafkaControllerComponentTest.when_publish_price_is_77_expect_Success.json"
        );
        workEventProducer.send(keyValueAvro.getKey(), keyValueAvro.getValue());
        keyValue = priceConsumer.pollLastKeyValue();
        Assertions.assertEquals(keyValueAvro.getKey(), keyValue.getKey());
        Assertions.assertEquals(keyValueAvro.getValue(), keyValue.getValue());
    }
}
