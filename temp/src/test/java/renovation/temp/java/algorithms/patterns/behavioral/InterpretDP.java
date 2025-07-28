/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.temp.java.algorithms.patterns.behavioral;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class InterpretDP {

    interface Expression {
        boolean interpret(String context);
    }

    @AllArgsConstructor
    static class TerminateExpression implements Expression {
        private final String expression;

        @Override
        public boolean interpret(String context) {
            return expression.toLowerCase().contains(context.toLowerCase());
        }
    }

    @AllArgsConstructor
    static class AddExpression implements Expression {
        private final List<Expression> expressions = new ArrayList<>();

        public void add(Expression expression) {
            this.expressions.add(expression);
        }

        @Override
        public boolean interpret(String context) {
            return !expressions.stream().filter(e -> !e.interpret(context)).findAny().isPresent();
        }
    }

    @Test
    public void test() {

        // When
        var addExpression = spy(new AddExpression());
        var t1 = spy(new TerminateExpression("hello, there"));
        var t2 = spy(new TerminateExpression("ok, now"));
        var t3 = spy(new TerminateExpression("yes, woman"));

        addExpression.add(t1);
        addExpression.add(t2);
        addExpression.add(t3);

        // Then
        assertEquals(true, addExpression.interpret("O"));
        assertEquals(false, addExpression.interpret("now"));

        verify(addExpression, times(2)).interpret(any());
        verify(t1, times(2)).interpret(any());
        verify(t2, times(1)).interpret(any());
        verify(t3, times(1)).interpret(any());
    }
}
