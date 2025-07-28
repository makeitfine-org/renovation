/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.temp.java.algorithms.patterns.behavioral;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class StateDP {

    interface State {
        void handleRequest();
    }

    static class StateImpl implements State {
        private static Logger LOG = LoggerFactory.getLogger(StateImpl.class);

        @Override
        public void handleRequest() {
            LOG.info("handleRequest");
        }
    }

    static class StateImpl2 implements State {
        private static Logger LOG = LoggerFactory.getLogger(StateImpl.class);

        @Override
        public void handleRequest() {
            LOG.info("handleRequest 2");
        }
    }

    static class Context {
        private State state;

        void setState(State state) {
            this.state = state;
        }

        void request() {
            state.handleRequest();
        }
    }

    @Test
    public void test() {
        // When
        var state = spy(new StateImpl());
        var state2 = spy(new StateImpl2());

        var context = spy(new Context());

        context.setState(state);
        context.request();

        context.setState(state2);
        context.request();

        // Then
        verify(state, times(1)).handleRequest();
        verify(state2, times(1)).handleRequest();

        verify(context, times(2)).request();
    }
}
