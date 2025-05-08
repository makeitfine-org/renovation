package renovation.event.service.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    public KafkaController(
            WorkEventMapper workEventMapper,
            WorkEventKafkaProducer workEventProducer
    ) {
        this.workEventMapper = workEventMapper;
        this.workEventProducer = workEventProducer;
    }

    @PostMapping("/publish")
    @ResponseStatus(HttpStatus.OK)
    public void publishMessage(@RequestBody WorkEventRequest request) {
        var key = workEventMapper.toAvroWorkEventKey(request);
        var data = workEventMapper.toAvroWorkEvent(request);

        workEventProducer.send(key, data);
    }
}
