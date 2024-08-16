/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2024
 */

package renovation.temp.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class Java11Test {

    @Test
    public void newStringMethods() {
        String multilineString = "Baeldung helps \n \n developers \n explore Java.";

        var lines = multilineString
                .lines()
                .filter(line -> !line.isBlank())
                .map(String::strip)
                .collect(Collectors.toList());

        assertThat(lines).containsExactly("Baeldung helps", "developers", "explore Java.");
    }

    @Test
    public void newFileMethods() throws IOException {
        Path filePath = Files.writeString(
                Files.createTempFile(Files.createTempDirectory("abc"), "demo", ".txt"),
                "Sample text"
        );

        String fileContent = Files.readString(filePath);

        assertThat(fileContent).isEqualTo("Sample text");
    }

    @Test
    public void collectionToArrayMethod() {
        List<String> sampleList = Arrays.asList("Java", "Kotlin");
        String[] sampleArray = sampleList.toArray(String[]::new);
        assertThat(sampleArray).containsExactly("Java", "Kotlin");
    }

    @Test
    public void notPredicateMethods() {
        String multilineString = "Baeldung helps \n \n developers \n explore Java.";

        var lines = multilineString
                .lines()
                .filter(Predicate.not(String::isBlank))
                .collect(Collectors.toList());

        assertThat(lines).containsExactly("Baeldung helps ", " developers ", " explore Java.");
    }

    @Test
    public void nestedControl() {
        var m = new Main();
        m.new Nested().nestedPublic();
    }

    public class Main {

        public void myPublic() {
        }

        private void myPrivate() {
        }

        class Nested {

            public void nestedPublic() {
                myPrivate();
            }
        }
    }
}
