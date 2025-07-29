/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.temp.java.applications.patterns.behavioral;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TemplateMethodDP {

    static abstract class Drink {

        public abstract String add();

        public String boil() {
            return "boil";
        }

        public String pour() {
            return "pour";
        }

        public String make() {
            return String.format("%s, %s, %s", add(), boil(), pour());
        }
    }

    static class Coffee extends Drink {
        @Override
        public String add() {
            return "coffee";
        }
    }

    static class Tea extends Drink {
        @Override
        public String add() {
            return "tea";
        }
    }

    @Test
    public void test() {

        // When
        var coffee = spy(new Coffee());
        var tea = spy(new Tea());

        // Then
        assertEquals("coffee, boil, pour", coffee.make());
        assertEquals("tea, boil, pour", tea.make());

        verify(coffee, times(1)).add();
        verify(coffee, times(1)).boil();
        verify(coffee, times(1)).pour();
        verify(coffee, times(1)).make();

        verify(tea, times(1)).add();
        verify(tea, times(1)).boil();
        verify(tea, times(1)).pour();
        verify(tea, times(1)).make();
    }
}
