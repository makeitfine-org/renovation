/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.temp.java.algorithms.patterns.creational;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PrototypeDP {

    interface Cookie {

        Object clone();

        double getAmountOfSugar();

        String getColor();

        List<Object> getObjects();
    }

    @Getter
    @AllArgsConstructor
    static class ChocolateCookie implements Cookie, Cloneable {
        private final double amountOfSugar;
        private final String color;
        private final List<Object> objects;

        @Override
        public Object clone() {
            try {
                return super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @AllArgsConstructor
    static class CookieMachine {
        private final Cookie cookie;

        public Cookie makeCookie() {
            return (Cookie) cookie.clone();
        }
    }

    @Test
    public void test() {

        // When
        var cookie = spy(
                new ChocolateCookie(
                        1,
                        "yellow",
                        Lists.newArrayList(new Object(), new Object()))
        );

        var cookieMachine = spy(new CookieMachine(cookie));
        var clonedCookie = spy(cookieMachine.makeCookie());

        assertNotEquals(clonedCookie, cookie);
        assertEquals(clonedCookie.getAmountOfSugar(), cookie.getAmountOfSugar());
        assertEquals(clonedCookie.getColor(), cookie.getColor());
        assertEquals(clonedCookie.getObjects(), cookie.getObjects());

        verify(cookie, times(1)).clone();
        verify(cookieMachine, times(1)).makeCookie();

        verify(clonedCookie, never()).clone();
    }
}
