/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.temp.java.applications.patterns.behavioral;

import lombok.NonNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class StrategyDP {

    interface SpellCheckerStrategy {
        String check();
    }

    static class EnglishSpellCheckerStrategy implements SpellCheckerStrategy {
        @Override
        public String check() {
            return "English checker";
        }
    }

    static class SpanishSpellCheckerStrategy implements SpellCheckerStrategy {
        @Override
        public String check() {
            return "Spanish checker";
        }
    }

    static class TextEditor {
        private SpellCheckerStrategy strategy = new EnglishSpellCheckerStrategy();

        void setChecker(@NonNull SpellCheckerStrategy strategy) {
            this.strategy = strategy;
        }

        String check() {
            return strategy.check();
        }
    }

    @Test
    public void test() {

        // When
        var englishChecker = spy(new EnglishSpellCheckerStrategy());
        var spanishChecker = spy(new SpanishSpellCheckerStrategy());

        var textEditor = spy(new TextEditor());

        // Then
        assertEquals("English checker", textEditor.check());
        textEditor.setChecker(spanishChecker);
        assertEquals("Spanish checker", textEditor.check());
        textEditor.setChecker(englishChecker);
        assertEquals("English checker", textEditor.check());

        var e = assertThrows(
                NullPointerException.class,
                () -> {
                    textEditor.setChecker(null);
                    textEditor.setChecker(null);
                }
        );
        assertEquals("strategy is marked non-null but is null", e.getMessage());

        verify(englishChecker, times(1)).check();
        verify(spanishChecker, times(1)).check();

        verify(textEditor, times(3)).check();
        verify(textEditor, times(3)).setChecker(any());
    }
}
