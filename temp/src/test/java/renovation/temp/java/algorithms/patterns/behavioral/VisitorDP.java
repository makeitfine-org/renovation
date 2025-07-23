/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.temp.java.algorithms.patterns.behavioral;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class VisitorDP {

    interface Part {
        void accept(Visitor visitor);
    }

    static class MousePart implements Part {
        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }

    static class KeyboardPart implements Part {
        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }

    static class ComputerPart implements Part {

        private List<Part> parts = List.of(new MousePart(), new KeyboardPart());

        @Override
        public void accept(Visitor visitor) {
            for (Part part : parts) {
                part.accept(visitor);
            }

            visitor.visit(this);
        }
    }

    interface Visitor {
        String visit(MousePart part);

        String visit(KeyboardPart part);

        String visit(ComputerPart part);
    }

    static class VisitorImpl implements Visitor {
        @Override
        public String visit(MousePart part) {
            return visitIn(part);
        }

        @Override
        public String visit(KeyboardPart part) {
            return visitIn(part);
        }

        @Override
        public String visit(ComputerPart part) {
            return visitIn(part);
        }

        private static <T> String visitIn(T part) {
            return part.getClass().getSimpleName();
        }
    }

    public static void main(String[] args) {
    }

    @Test
    public void test() {

        // When
        var computerPart = new ComputerPart();
        var visitor = Mockito.spy(new VisitorImpl());

        computerPart.accept(visitor);

        // Then - Capture the arguments passed to visit(MousePart) and visit(KeyboardPart)
        ArgumentCaptor<MousePart> mouseCaptor = ArgumentCaptor.forClass(MousePart.class);
        ArgumentCaptor<KeyboardPart> keyboardCaptor = ArgumentCaptor.forClass(KeyboardPart.class);
        ArgumentCaptor<ComputerPart> computerCaptor = ArgumentCaptor.forClass(ComputerPart.class);

        verify(visitor, times(1)).visit(mouseCaptor.capture());
        verify(visitor, times(1)).visit(keyboardCaptor.capture());
        verify(visitor, times(1)).visit(computerCaptor.capture());

        // Assert captured argument class names
        assertEquals("MousePart", mouseCaptor.getValue().getClass().getSimpleName());
        assertEquals("KeyboardPart", keyboardCaptor.getValue().getClass().getSimpleName());
        assertEquals("ComputerPart", computerCaptor.getValue().getClass().getSimpleName());

        // ✅ To check return values, manually call visit methods and assert
        assertEquals("MousePart", visitor.visit(mouseCaptor.getValue()));
        assertEquals("KeyboardPart", visitor.visit(keyboardCaptor.getValue()));
        assertEquals("ComputerPart", visitor.visit(computerCaptor.getValue()));
    }
}
