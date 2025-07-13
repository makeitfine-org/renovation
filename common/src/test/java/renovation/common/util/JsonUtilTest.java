/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonUtilTest {
    private static final String JSON_CONTENT = """
            {
                    "field1": 1,
                    "field2": {
                        "fieldInside": "abc"
                    },
                    "field3": [
                        1,
                        2,
                        4
                    ]
                }
            """;

    @Test
    void simplify() {
        assertEquals(
                "{\"field1\":1,\"field2\":{\"fieldInside\":\"abc\"},\"field3\":[1,2,4]}",
                JsonUtil.simplify(JSON_CONTENT)
        );
    }
}
