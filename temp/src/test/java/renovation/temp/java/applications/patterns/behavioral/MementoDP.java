/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.temp.java.applications.patterns.behavioral;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MementoDP {

    @Getter
    @AllArgsConstructor
    static class Memento {
        private final String state;
    }

    static class Originator {

        @Setter
        private String content = "";

        public void write(String text) {
            content += text;
        }

        public Memento getMemento() {
            return new Memento(content);
        }

        public void restore(Memento memento) {
            this.content = memento.getState();
        }
    }

    static class CareTaker {
        public static Memento MEMENTO = new Memento("");

        private Stack<Memento> mementos = new Stack<>();

        public void push(Memento memento) {
            mementos.push(memento);
        }

        public Memento pop() {
            if (!mementos.isEmpty()) {
                return mementos.pop();
            }
            return MEMENTO;
        }
    }

    @Test
    public void test() {

        // When
        var originator = spy(new Originator());
        var history = spy(new CareTaker());

        originator.write("content1");
        history.push(originator.getMemento());
        originator.write(", content2");
        history.push(originator.getMemento());
        originator.write(", content3");

        // Then
        assertEquals("content1, content2, content3", originator.getMemento().getState());
        originator.restore(history.pop());
        assertEquals("content1, content2", originator.getMemento().getState());
        originator.restore(history.pop());
        assertEquals("content1", originator.getMemento().getState());
        originator.restore(history.pop());
        assertEquals("", originator.getMemento().getState());
        originator.restore(history.pop());
        assertEquals("", originator.getMemento().getState());

        // Then
        verify(history, times(2)).push(any());
        verify(history, times(4)).pop();

        verify(originator, times(7)).getMemento();
        verify(originator, times(4)).restore(any());
    }
}
