/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.temp.java.algorithms.patterns.behavioral;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MediatorDP {

    interface Airplane {
        String requestTakeoff();

        String requestLanding();

        String notify(String message);
    }

    interface ControlTower {
        String requestTakeoff(Airplane airplane);

        String requestLanding(Airplane airplane);
    }

    @AllArgsConstructor
    static class AirplaneImpl implements Airplane {
        private final ControlTower controlTower;

        @Override
        public String requestTakeoff() {
            return controlTower.requestTakeoff(this);
        }

        @Override
        public String requestLanding() {
            return controlTower.requestLanding(this);
        }

        @Override
        public String notify(String message) {
            return message;
        }
    }

    @Setter
    static class ControlTowerImpl implements ControlTower {
        private Airplane airplane1;
        private Airplane airplane2;

        @Override
        public String requestTakeoff(Airplane airplane) {
            return otherAirplane(airplane, "takeoff");
        }

        @Override
        public String requestLanding(Airplane airplane) {
            return otherAirplane(airplane, "landing");
        }

        private String otherAirplane(Airplane airplane, String message) {
            var notified = airplane == airplane1 ? Pair.of(airplane2, 1) : Pair.of(airplane1, 2);

            return notified.getLeft().notify("Gets " + message + " message from airplane " + notified.getRight());
        }
    }

    @Test
    public void test() {

        // When
        var controlTower = spy(new ControlTowerImpl());

        var airplane1 = spy(new AirplaneImpl(controlTower));
        var airplane2 = spy(new AirplaneImpl(controlTower));

        controlTower.setAirplane1(airplane1);
        controlTower.setAirplane2(airplane2);

        // Then
        assertEquals("Gets takeoff message from airplane 1", airplane1.requestTakeoff());
        verify(airplane2, times(1)).notify("Gets takeoff message from airplane 1");
        verify(controlTower, times(1)).requestTakeoff(airplane1);

        reset(airplane1);
        reset(airplane2);
        reset(controlTower);

        assertEquals("Gets takeoff message from airplane 2", airplane2.requestTakeoff());
        verify(airplane1, times(1)).notify("Gets takeoff message from airplane 2");
        verify(controlTower, times(1)).requestTakeoff(airplane2);

        reset(airplane1);
        reset(airplane2);
        reset(controlTower);

        assertEquals("Gets landing message from airplane 1", airplane1.requestLanding());
        verify(airplane2, times(1)).notify("Gets landing message from airplane 1");
        verify(controlTower, times(1)).requestLanding(airplane1);

        reset(airplane1);
        reset(airplane2);
        reset(controlTower);

        assertEquals("Gets landing message from airplane 2", airplane2.requestLanding());
        verify(airplane1, times(1)).notify("Gets landing message from airplane 2");
        verify(controlTower, times(1)).requestLanding(airplane2);

        reset(airplane1);
        reset(airplane2);
        reset(controlTower);
    }
}
