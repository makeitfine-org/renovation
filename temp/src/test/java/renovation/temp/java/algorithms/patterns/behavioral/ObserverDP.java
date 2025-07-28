/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.temp.java.algorithms.patterns.behavioral;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ObserverDP {

    interface Observer {
        void update(String event);
    }

    interface Subject {
        void addObserver(Observer observer);

        void removeObserver(Observer observer);

        void notify(String event);
    }

    static class ObserverImpl implements Observer {
        private static Logger LOG = LoggerFactory.getLogger(ObserverImpl.class);

        @Override
        public void update(String event) {
            LOG.info(event);
        }
    }

    static class SubjectImpl implements Subject {
        private final List<Observer> observers = new ArrayList<>();

        @Override
        public void addObserver(Observer observer) {
            observers.add(observer);
        }

        @Override
        public void removeObserver(Observer observer) {
            observers.remove(observer);
        }

        @Override
        public void notify(String event) {
            observers.forEach(observer -> observer.update(event));
        }
    }

    @Test
    public void test() {

        // When
        var observer1 = spy(new ObserverImpl());
        var observer2 = spy(new ObserverImpl());
        var observer3 = spy(new ObserverImpl());

        var subject = spy(new SubjectImpl());
        subject.addObserver(observer1);
        subject.addObserver(observer2);
        subject.addObserver(observer3);

        subject.notify("hello");
        subject.notify("hello");

        // Then
        verify(observer1, times(2)).update("hello");
        verify(observer2, times(2)).update("hello");
        verify(observer3, times(2)).update("hello");

        verify(subject, times(2)).notify("hello");
    }
}
