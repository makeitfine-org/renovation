/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Util class for different test purpose
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestUtil {

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
