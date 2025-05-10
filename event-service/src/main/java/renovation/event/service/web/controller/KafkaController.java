package renovation.event.service.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import renovation.event.service.service.mapper.WorkEventMapper;
import renovation.event.service.service.producer.WorkEventKafkaProducer;
import renovation.event.service.web.Route;
import renovation.event.service.web.dto.WorkEventRequest;

@RestController
@RequestMapping(value = Route.KAFKA, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class KafkaController {

    private final WorkEventMapper workEventMapper;
    private final WorkEventKafkaProducer workEventProducer;
    private final String storeName;
    private final StreamsBuilderFactoryBean factoryBean;

    public KafkaController(
            WorkEventMapper workEventMapper,
            WorkEventKafkaProducer workEventProducer,
            @Value("${spring.kafka.streams.store.name}")
            String storeName,
            StreamsBuilderFactoryBean factoryBean
    ) {
        this.workEventMapper = workEventMapper;
        this.workEventProducer = workEventProducer;
        this.storeName = storeName;
        this.factoryBean = factoryBean;
    }

    @PostMapping("/publish")
    @ResponseStatus(HttpStatus.OK)
    public void publishMessage(@RequestBody WorkEventRequest request) {
        var key = workEventMapper.toAvroWorkEventKey(request);
        var data = workEventMapper.toAvroWorkEvent(request);

        workEventProducer.send(key, data);
    }

    @PostMapping("/publish/price")
    @ResponseStatus(HttpStatus.OK)
    public void publishPriceMessage(
            @RequestBody WorkEventRequest request,
            @Value("${spring.kafka.topic.price.name}") String topicName) {
        var key = workEventMapper.toAvroWorkEventKey(request);
        var data = workEventMapper.toAvroWorkEvent(request);

        workEventProducer.send(key, data, topicName);
    }

    @GetMapping("/work/{workId}")
    @ResponseStatus(HttpStatus.OK)
    public String getPriceSumByWorkId(@PathVariable String workId) {
        KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();
        ReadOnlyKeyValueStore<String, Long> priceSumStore = kafkaStreams
                .store(StoreQueryParameters.fromNameAndType(storeName, QueryableStoreTypes.keyValueStore()));

        return String.format("Price sum by work id %s is %s", workId, priceSumStore.get(workId));
    }
}
