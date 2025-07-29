/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.temp.java.applications.patterns.behavioral;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CommandDP {

    interface Command {
        String execute();
    }

    @AllArgsConstructor
    static class TurnOnCommand implements Command {
        private final Device device;

        @Override
        public String execute() {
            return device.turnOn();
        }
    }

    @AllArgsConstructor
    static class TurnOffCommand implements Command {
        private final Device device;

        @Override
        public String execute() {
            return device.turnOff();
        }
    }

    @AllArgsConstructor
    static class AdjustCommand implements Command {
        private final TV tv;

        @Override
        public String execute() {
            return tv.adjustVolume();
        }
    }

    interface Device {
        String turnOn();

        String turnOff();
    }

    static class TV implements Device {
        @Override
        public String turnOn() {
            return "turn on TV";
        }

        @Override
        public String turnOff() {
            return "turn off TV";
        }

        public String adjustVolume() {
            return "adjust volume of TV";
        }
    }

    static class Invoker {

        @Setter
        private Command command;

        public String press() {
            return command.execute();
        }
    }

    @Test
    public void test() {

        // When
        var tv = spy(new TV());

        var turnOnCommand = spy(new TurnOnCommand(tv));
        var turnOffCommand = spy(new TurnOffCommand(tv));
        var adjustCommand = spy(new AdjustCommand(tv));

        var remoteControl = spy(new Invoker());

        // Then
        var e = assertThrows(
                NullPointerException.class,
                () -> {
                    remoteControl.press();
                }
        );
        assertEquals(
                "Cannot invoke \"renovation.temp.java.applications.patterns.behavioral" +
                        ".CommandDP$Command.execute()\" because \"<local2>.command\" is null",
                e.getMessage()
        );

        remoteControl.setCommand(turnOnCommand);
        assertEquals("turn on TV", remoteControl.press());
        remoteControl.setCommand(turnOffCommand);
        assertEquals("turn off TV", remoteControl.press());
        remoteControl.setCommand(adjustCommand);
        assertEquals("adjust volume of TV", remoteControl.press());

        verify(turnOnCommand, times(1)).execute();
        verify(turnOffCommand, times(1)).execute();
        verify(adjustCommand, times(1)).execute();

        verify(tv, times(1)).turnOn();
        verify(tv, times(1)).turnOff();
        verify(tv, times(1)).adjustVolume();

        verify(remoteControl, times(3)).setCommand(any());
        verify(remoteControl, times(4)).press();
    }
}
