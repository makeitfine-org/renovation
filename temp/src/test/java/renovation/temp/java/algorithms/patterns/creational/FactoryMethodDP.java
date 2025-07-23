/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.temp.java.algorithms.patterns.creational;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class FactoryMethodDP {

    interface Shape {
        double square();
    }

    interface ShapeFactory {
        Shape createShape(int numberOfCorners);
    }

    @AllArgsConstructor
    static class Triangle implements Shape {

        private final double base;
        private final double height;

        @Override
        public double square() {
            return 0.5 * base * height;
        }
    }

    @AllArgsConstructor
    static class Rectangle implements Shape {

        private final double width;
        private final double height;

        @Override
        public double square() {
            return width * height;
        }
    }

    static class ShapeFactoryImpl implements ShapeFactory {
        @Override
        public Shape createShape(int numberOfCorners) {
            return switch (numberOfCorners) {
                case 3 -> new Triangle(3, 2);
                case 4 -> new Rectangle(3, 5);
                default -> throw new NotImplementedException("No shape available");
            };
        }
    }

    @Test
    public void test() {

        // When
        var factory = spy(new ShapeFactoryImpl());

        var shape = spy(factory.createShape(3));
        assertEquals(3.0, shape.square());
        assertEquals(Triangle.class, shape.getClass());
        verify(shape, times(1)).square();

        shape = spy(factory.createShape(4));
        assertEquals(15.0, shape.square());
        assertEquals(Rectangle.class, shape.getClass());
        verify(shape, times(1)).square();

        var e = assertThrows(
                NotImplementedException.class,
                () -> {
                    spy(factory.createShape(5));
                }
        );
        assertEquals("No shape available", e.getMessage());

        verify(factory, times(3)).createShape(any(Integer.class));
    }
}
