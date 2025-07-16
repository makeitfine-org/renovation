/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2024
 */

package renovation.temp.java;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

interface ITemp {

    default String method() throws IllegalArgumentException {
        return "default method";
    }
}

class Temp implements ITemp {

    @Override
    public String method() {
        return ITemp.super.method() + " override";
    }
}

class Java8Test {

    @Test
    public void newStringMethods() {
        assertEquals("default method", new ITemp() {
        }.method());

        assertEquals("default method override", new Temp().method());
    }
}
