/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.temp.java.applications.patterns.creational;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BuilderDP {

    @AllArgsConstructor
    @Data
    static class Pizza {
        private final String meat;
        private final String cheese;
    }

    interface PizzaBuilder {
        Pizza buildPizza();
    }

    @AllArgsConstructor
    static class PizzaABuilder implements PizzaBuilder {

        @Override
        public Pizza buildPizza() {
            return new Pizza("meatA", "cheeseA");
        }
    }

    @AllArgsConstructor
    static class PizzaBBuilder implements PizzaBuilder {

        @Override
        public Pizza buildPizza() {
            return new Pizza("meatB", "cheeseB");
        }
    }

    @Setter
    static class PizzaMachine {
        private PizzaBuilder pizzaBuilder;

        public Pizza getPizza() {
            return pizzaBuilder.buildPizza();
        }
    }

    @Test
    public void test() {

        // When
        var pizzaMachine = spy(new PizzaMachine());

        PizzaBuilder pizzaBuilder = spy(new PizzaABuilder());
        pizzaMachine.setPizzaBuilder(pizzaBuilder);
        var pizza = pizzaMachine.getPizza();
        assertEquals(pizza.getCheese(), "cheeseA");
        assertEquals(pizza.getMeat(), "meatA");
        verify(pizzaBuilder, times(1)).buildPizza();

        pizzaBuilder = spy(new PizzaBBuilder());
        pizzaMachine.setPizzaBuilder(pizzaBuilder);
        pizza = pizzaMachine.getPizza();
        assertEquals(pizza.getCheese(), "cheeseB");
        assertEquals(pizza.getMeat(), "meatB");
        verify(pizzaBuilder, times(1)).buildPizza();
    }
}
