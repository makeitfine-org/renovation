/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.temp.java.applications.patterns.structural;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ProxyDP {

    interface Show {
        String display();
    }

    static class ShowImpl implements Show {
        @Override
        public String display() {
            return "show it";
        }
    }

    @AllArgsConstructor
    static class ProxyShow implements Show {
        private Show show;

        @Override
        public String display() {
            if (show == null) {
                show = new ShowImpl();
            }
            return show.display();
        }
    }

    @Test
    public void test() {
        // When
        Show proxy = spy(new ProxyShow(null));
        var display = proxy.display();

        assertEquals("show it", display);
        verify(proxy, times(1)).display();
    }
}
