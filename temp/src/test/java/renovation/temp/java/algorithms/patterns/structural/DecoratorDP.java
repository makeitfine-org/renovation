/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.temp.java.algorithms.patterns.structural;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DecoratorDP {

    interface Coffee {
        String getDesc();

        double getCost();
    }

    static class PlainCoffee implements Coffee {
        @Override
        public String getDesc() {
            return "Plain coffee";
        }

        @Override
        public double getCost() {
            return 1.5;
        }
    }

    @AllArgsConstructor
    static abstract class CoffeeDecorator implements Coffee {
        private final Coffee coffee;

        @Override
        public String getDesc() {
            return coffee.getDesc();
        }

        @Override
        public double getCost() {
            return coffee.getCost();
        }
    }

    static class MilkCoffeeDecorator extends CoffeeDecorator {

        public MilkCoffeeDecorator(Coffee coffee) {
            super(coffee);
        }

        @Override
        public String getDesc() {
            return super.getDesc() + ", with milk";
        }

        @Override
        public double getCost() {
            return super.getCost() + 0.25;
        }
    }

    static class SugarCoffeeDecorator extends CoffeeDecorator {

        public SugarCoffeeDecorator(Coffee coffee) {
            super(coffee);
        }

        @Override
        public String getDesc() {
            return super.getDesc() + ", with sugar";
        }

        @Override
        public double getCost() {
            return super.getCost();
        }
    }

    @Test
    public void test() {
        // When
        Coffee coffee = spy(new PlainCoffee());
        Coffee milkCoffee = spy(new MilkCoffeeDecorator(coffee));

        Coffee sugarMilkCoffee = spy(new SugarCoffeeDecorator(milkCoffee));

        assertEquals(1.75, sugarMilkCoffee.getCost(), 0.001);
        assertEquals("Plain coffee, with milk, with sugar", sugarMilkCoffee.getDesc());

        verify(coffee, times(1)).getCost();
        verify(coffee, times(1)).getDesc();

        verify(milkCoffee, times(1)).getCost();
        verify(milkCoffee, times(1)).getDesc();

        verify(sugarMilkCoffee, times(1)).getCost();
        verify(sugarMilkCoffee, times(1)).getDesc();
    }
}
