/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.temp.java.applications.patterns.structural;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class FlyweightDP {

    interface Word {
        String statistics();
    }

    @AllArgsConstructor
    static class WordImpl implements Word {
        private final String word;

        @Override
        public String statistics() {
            return String.format("word: %s, length: %d", word, word.length());
        }
    }

    static class WordFactory {
        private final Map<String, Word> cache = new HashMap<>();

        public Word get(String word) {
            return cache.computeIfAbsent(word.toLowerCase(), WordImpl::new);
        }
    }


    @Test
    public void test() {

        // When
        var wordFactory = spy(new WordFactory());

        var word1 = wordFactory.get("word1");
        var word2 = wordFactory.get("Timmy");
        var word1C = wordFactory.get("Word1");

        // Then
        assertEquals("word: word1, length: 5", word1.statistics());
        assertEquals("word: timmy, length: 5", word2.statistics());
        assertEquals("word: word1, length: 5", word1C.statistics());
        assertEquals(word1, word1C);

        verify(wordFactory, times(3)).get(anyString());
    }
}
