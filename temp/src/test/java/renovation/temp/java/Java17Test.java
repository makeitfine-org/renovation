/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2024
 */

package renovation.temp.java;

import org.junit.jupiter.api.Test;

import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Java17Test {

    @Test
    public void switchNew() {
        DayOfWeek dayOfWeek = DayOfWeek.WEDNESDAY;
        boolean freeDay = switch (dayOfWeek) {
            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> false;
            case SATURDAY, SUNDAY -> true;
        };

        assertFalse(freeDay);
    }

    @Test
    public void switchNewOther() {
        DayOfWeek dayOfWeek = DayOfWeek.SUNDAY;
        boolean freeDay = switch (dayOfWeek) {
            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> {
                System.out.println("Work work work");
                yield false;
            }
            case SATURDAY, SUNDAY -> {
                System.out.println("Yey, a free day!");
                yield true;
            }
        };

        assertTrue(freeDay);
    }

    public void instanceOf() {
        Object o = "abc";

        if (o instanceof String myO && !myO.isEmpty()) {
            assertEquals(3, myO.length());
        }
    }

    public abstract sealed class Animal
            permits Dog, Cat {
    }

    public final class Dog extends Animal {
    }

    public final class Cat extends Animal {
    }

    @Test
    public void sealedClass() {
        Animal animal = new Dog();

        var value = switch (animal) {
            case Dog d -> "a";
            case Cat c -> "b";
        };

        assertEquals("a", value);
    }

    @Test
    public void testBlock() {
        var word = """
                Tempo
                it
                """;

        assertEquals("Tempo\nit\n", word);
    }

    @Test
    public void improvedNPE() {
        record Street(String name) {
        }
        record Town(String name, Street street) {
        }
        record Country(String name, Town town) {
        }

        var country = new Country("state", new Town("town", null));

        var e = assertThrows(
                NullPointerException.class,
                () -> {
                    var streetName = country.town.street().name;
                }
        );

        assertEquals(
                "Cannot read field \"name\" because the return value of " +
                        "\"renovation.temp.java.Java17Test$1Town.street()\" is null",
                e.getMessage()
        );
    }

    @Test
    public void numberFormat() {
        var shortFormat = NumberFormat.getCompactNumberInstance(Locale.ENGLISH, NumberFormat.Style.SHORT);
        assertEquals("1K", shortFormat.format(1000));

        shortFormat = NumberFormat.getCompactNumberInstance(Locale.ENGLISH, NumberFormat.Style.LONG);
        assertEquals("1 thousand", shortFormat.format(1000));
    }

    @Test
    public void datePeriod() {
        var timeOfDayFormatter = DateTimeFormatter.ofPattern("B");
        assertEquals("in the morning", timeOfDayFormatter.format(LocalTime.of(8, 0)));
        assertEquals("at night", timeOfDayFormatter.format(LocalTime.of(23, 0)));
        assertEquals("midnight", timeOfDayFormatter.format(LocalTime.of(0, 0)));
    }
}
