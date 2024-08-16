/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2024
 */

package renovation.temp.java;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Java21Test {

    enum Color {RED, GREEN, BLUE}

    record Point(int x, int y) {
    }

    record ColoredPoint(Point point, Color color) {
    }

    record RandomPoint(ColoredPoint cp) {
    }

    @Test
    public void recordPattern() {
        var r = new RandomPoint(new ColoredPoint(new Point(1, 2), Color.GREEN));

        if (r instanceof RandomPoint(ColoredPoint(var point, var color))) {
            assertEquals(new Point(1, 2), point);
            assertEquals(Color.GREEN, color);
        }
    }

    public record Wrapper<T>(T t, String description) {
    }

    @Test
    public void recordPatternWithGeneric() {
        Wrapper<Integer> wrapper = new Wrapper<>(125, "Description");
        if (wrapper instanceof Wrapper<Integer>(var number, var description)) {
            assertEquals(125, number);
        }
    }

    @Test
    public void recordPatternWithSwitch() {
        Object obj = new Point(5, 5);
        Object resultObj = switch (obj) {
            case Point point -> point.x();
            default -> "default";
        };

        assertEquals(5, resultObj);

        //========================

        obj = 555;
        resultObj = switch (obj) {
            case Point point -> point.x();
            default -> "default";
        };

        assertEquals("default", resultObj);

        //========================

        var point = new Point(5, 55);
        resultObj = switch (point) {
            case Point(var x, var y) -> y;
            default -> "default";
        };

        assertEquals(55, resultObj);
    }

    class One {
        private String str;

        public One(String str) {
            this.str = str;
        }

        public String getStr() {
            return str;
        }
    }

    private String switcher(One one) {
        return switch (one) {
            case null -> "no";
            case One oneInside when oneInside.getStr() == "qa" -> "qa";
            case One oneInside when oneInside.getStr() == "abc" -> "abc";
            default -> "default";
        };
    }

    @Test
    public void switchTest() {
        assertEquals("no", switcher(null));
        assertEquals("qa", switcher(new One("qa")));
        assertEquals("abc", switcher(new One("abc")));
        assertEquals("default", switcher(new One("abc333")));
    }
}
