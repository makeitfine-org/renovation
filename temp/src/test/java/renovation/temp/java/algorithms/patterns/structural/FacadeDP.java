/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.temp.java.algorithms.patterns.structural;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class FacadeDP {

    static class Car {
        String engineStart() {
            return "engine started";
        }

        String engineStop() {
            return "engine stopped";
        }
    }

    static class Signalization {
        String turnOn() {
            return "signalization on";
        }

        String turnOff() {
            return "signalization off";
        }
    }

    @AllArgsConstructor
    static class CarFacade {
        private final Car car;
        private final Signalization signalization;

        void on() {
            signalization.turnOff();
            car.engineStart();
        }

        void off() {
            car.engineStop();
            signalization.turnOn();
        }
    }

    @Test
    public void test() {
        // When
        var car = spy(new Car());
        var signalization = spy(new Signalization());
        var carFacade = spy(new CarFacade(car, signalization));

        carFacade.on();
        carFacade.off();

        // Then
        verify(signalization, times(1)).turnOff();
        verify(signalization, times(1)).turnOn();

        verify(car, times(1)).engineStart();
        verify(car, times(1)).engineStop();

        verify(carFacade, times(1)).on();
        verify(carFacade, times(1)).off();

        // ✅ Order verification:
        var inOrder = org.mockito.Mockito.inOrder(signalization, car);
        inOrder.verify(signalization).turnOff();
        inOrder.verify(car).engineStart();
        inOrder.verify(car).engineStop();
        inOrder.verify(signalization).turnOn();
    }
}
