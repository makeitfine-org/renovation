/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.temp.java.applications.patterns.creational;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PrototypeDP {

    interface Cookie {

        Object clone();

        double getAmountOfSugar();

        Integer getChocolateId();

        String getColor();

        List<Object> getObjects();
    }

    @Getter
    @AllArgsConstructor
    static class ChocolateCookie implements Cookie, Cloneable {
        private final double amountOfSugar;
        private final Integer chocolateId;
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
                        285.5,
                        Integer.valueOf(1850),
                        new String("yellow"),
                        Lists.newArrayList(new Object(), new Object()))
        );
        // And
        var cookieMachine = spy(new CookieMachine(cookie));
        var clonedCookie = spy(cookieMachine.makeCookie());
        // Then
        assertNotEquals(clonedCookie, cookie);
        assertEquals(clonedCookie.getAmountOfSugar(), cookie.getAmountOfSugar());
        assertEquals(clonedCookie.getChocolateId(), cookie.getChocolateId());
        assertEquals(clonedCookie.getColor(), cookie.getColor());
        assertEquals(clonedCookie.getObjects(), cookie.getObjects());

        assertNotSame(clonedCookie, cookie);
        assertNotSame(clonedCookie.getAmountOfSugar(), cookie.getAmountOfSugar());
        assertSame(clonedCookie.getChocolateId(), cookie.getChocolateId());
        assertSame(clonedCookie.getColor(), cookie.getColor());
        assertSame(clonedCookie.getObjects(), cookie.getObjects());

        verify(cookie, times(1)).clone();
        verify(cookieMachine, times(1)).makeCookie();

        verify(clonedCookie, never()).clone();
    }
}
