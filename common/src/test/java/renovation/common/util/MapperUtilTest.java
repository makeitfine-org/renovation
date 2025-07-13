/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.common.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapperUtilTest {
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
    void readFileContentFromProjectRoot() throws IOException {
        String testFileName = "temp_test_file.txt";
        var file = Paths.get(testFileName);
        Files.writeString(file, "any\nother content");

        try {
            assertEquals("any\nother content", MapperUtil.readFileContentFromProjectRoot(testFileName));
        } finally {
            Files.deleteIfExists(file);
        }
    }

    @Test
    void jsonFileContentFromSrcTestResources() throws IOException {
        String testFileName = "src/test/resources/json/temp_test_file.txt";
        var file = Paths.get(testFileName);

        // Ensure the json directory exists
        Files.createDirectories(file.getParent());

        Files.writeString(file, "{\"abc\":1}");

        try {
            assertEquals(
                    "{\"abc\":1}",
                    MapperUtil.jsonFileContentFromSrcTestResources("temp_test_file.txt")
            );
        } finally {
            Files.deleteIfExists(file);
        }
    }
}
