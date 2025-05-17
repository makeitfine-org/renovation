/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.event.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.testcontainers.shaded.org.apache.commons.lang3.tuple.ImmutablePair;
import renovation.event.service.kafka.avro.record.work.WorkEvent;
import renovation.event.service.kafka.avro.record.work.WorkEventKey;
import renovation.event.service.service.mapper.WorkEventMapper;
import renovation.event.service.web.dto.WorkEventRequest;

import static renovation.common.util.MapperUtil.OBJECT_MAPPER;
import static renovation.common.util.MapperUtil.jsonFileContentFromSrcTestResources;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KafkaUtil {

    public static final int STREAMS_INIT_TIME_WAIT = 45;

    //todo: change everywhere
    public static ImmutablePair<WorkEventKey, WorkEvent> keyValueFromJsonFileContentFromSrcTestResources(
            WorkEventMapper mapper,
            String path
    ) throws JsonProcessingException {
        var body = jsonFileContentFromSrcTestResources(
                path
        );
        var request = OBJECT_MAPPER.readValue(body, WorkEventRequest.class);

        var key = mapper.toAvroWorkEventKey(null);
        var data = mapper.toAvroWorkEvent(request);

        return ImmutablePair.of(key, data);
    }
}
