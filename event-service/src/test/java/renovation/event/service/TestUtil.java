/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.event.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.testcontainers.shaded.org.apache.commons.lang3.tuple.ImmutablePair;
import renovation.event.service.kafka.avro.record.work.WorkEvent;
import renovation.event.service.kafka.avro.record.work.WorkEventKey;
import renovation.event.service.service.mapper.WorkEventMapper;
import renovation.event.service.web.dto.WorkEventRequest;

import static renovation.event.service.util.Helper.OBJECT_MAPPER;
import static renovation.event.service.util.Helper.readFileContentFromProjectRoot;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestUtil { //todo: refactoring/move to common module

    public static final int STREAMS_INIT_TIME_WAIT = 45;

    private static final Gson GSON = new GsonBuilder().serializeNulls().create();

    /**
     * Represent pretty formater json as spaceless json raw.
     *
     * @param json pretty formatted json
     * @return spaceless json raw
     */
    public static String simplify(String json) {
        JsonElement el = JsonParser.parseString(json);
        return GSON.toJson(el);
    }

    /**
     * Read file content from src/test/resources/json/
     *
     * @param pathInSrcTestResources relative path
     * @return spaceless json raw
     */
    public static String jsonFileContentFromSrcTestResources(String pathInSrcTestResources) {
        return readFileContentFromProjectRoot("src/test/resources/json/" + pathInSrcTestResources);
    }

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
