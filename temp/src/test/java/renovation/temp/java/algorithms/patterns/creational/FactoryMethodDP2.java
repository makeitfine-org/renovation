/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.temp.java.algorithms.patterns.creational;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class FactoryMethodDP2 {

    interface Shape {
        double square();
    }

    interface ShapeFactory {
        Shape createShape();
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

    static class TriangleShapeFactory implements ShapeFactory {
        @Override
        public Shape createShape() {
            return new Triangle(5, 7);
        }
    }

    static class RectangleShapeFactory implements ShapeFactory {
        @Override
        public Shape createShape() {
            return new Rectangle(5, 1.5);
        }
    }

    @Test
    public void test() {

        // When
        var list = List.of(new TriangleShapeFactory(), new RectangleShapeFactory());

        var shape = spy(list.getFirst().createShape());
        assertEquals(17.5, shape.square());
        assertEquals(Triangle.class, shape.getClass());
        verify(shape, times(1)).square();

        shape = spy(list.getLast().createShape());
        assertEquals(7.5, shape.square());
        assertEquals(Rectangle.class, shape.getClass());
        verify(shape, times(1)).square();
    }
}
