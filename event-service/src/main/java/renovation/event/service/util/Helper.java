package renovation.event.service.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Helper {
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .setDateFormat(new SimpleDateFormat(DATE_TIME_FORMAT))
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    static {
        SimpleModule sm = new SimpleModule();
        sm.addDeserializer(Instant.class, new InstantDeserializer());
        OBJECT_MAPPER.registerModule(sm);
    }

    public static class InstantDeserializer extends JsonDeserializer<Instant> {
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");

        @SneakyThrows
        @Override
        public Instant deserialize(JsonParser p, DeserializationContext ctxt) {
            JsonNode value = p.getCodec().readTree(p);
            var creationDate = value.asText();

            try {
                return dateFormat.parse(creationDate).toInstant();
            } catch (ParseException e) {
                return Instant.parse(creationDate);
            }
        }
    }

    public static String readFileContentFromProjectRoot(String pathInProjectRoot) {
        try {
            return Files.readString(Path.of(pathInProjectRoot)).trim();
        } catch (IOException e) {
            throw new IllegalArgumentException(pathInProjectRoot);
        }
    }
}
