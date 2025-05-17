/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.neo4j.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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
}
