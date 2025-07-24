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

public class BridgeDP {

    interface TV {
        String turnOn();

        String turnOff();
    }

    static class SonyTV implements TV {
        @Override
        public String turnOn() {
            return "Sony TV [ON]";
        }

        @Override
        public String turnOff() {
            return "Sony TV [OFF]";
        }
    }

    static class LgTV implements TV {
        @Override
        public String turnOn() {
            return "LG TV [ON]";
        }

        @Override
        public String turnOff() {
            return "LG TV [OFF]";
        }
    }

    @AllArgsConstructor
    static abstract class RemoteControl {

        protected final TV tv;

        public abstract String on();

        public abstract String off();
    }

    static class StandardRemoteControl extends RemoteControl {

        public StandardRemoteControl(TV tv) {
            super(tv);
        }

        @Override
        public String on() {
            return tv.turnOn();
        }

        @Override
        public String off() {
            return tv.turnOff();
        }
    }

    static class ProRemoteControl extends RemoteControl {

        public ProRemoteControl(TV tv) {
            super(tv);
        }

        @Override
        public String on() {
            return tv.turnOn();
        }

        @Override
        public String off() {
            return tv.turnOff();
        }

        public String mute() {
            return tv.getClass().getSimpleName() + " is muted";
        }
    }

    @Test
    public void test() {

        // When
        TV sonyTV = spy(new SonyTV());
        TV lgTV = spy(new LgTV());

        // When
        RemoteControl remoteControl = spy(new StandardRemoteControl(sonyTV));
        var on = remoteControl.on();
        var off = remoteControl.off();

        //Then
        assertEquals("Sony TV [ON]", on);
        assertEquals("Sony TV [OFF]", off);
        verify(remoteControl, times(1)).on();
        verify(remoteControl, times(1)).off();
        verify(sonyTV, times(1)).turnOn();
        verify(sonyTV, times(1)).turnOff();

        // When
        remoteControl = spy(new ProRemoteControl(lgTV));
        on = remoteControl.on();
        off = remoteControl.off();
        var mute = ((ProRemoteControl) remoteControl).mute();

        //Then
        assertEquals("LG TV [ON]", on);
        assertEquals("LG TV [OFF]", off);
        assertEquals("LgTV is muted", mute);
        verify(remoteControl, times(1)).on();
        verify(remoteControl, times(1)).off();
        verify((ProRemoteControl) remoteControl, times(1)).mute();
        verify(lgTV, times(1)).turnOn();
        verify(lgTV, times(1)).turnOff();
        verify(lgTV, times(1)).getClass().getSimpleName();
    }
}
