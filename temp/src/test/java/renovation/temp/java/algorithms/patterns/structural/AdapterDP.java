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

public class AdapterDP {

    interface ModernInterface {
        int getNumber();
    }

    static class OldInterfaceClass {
        double getOldNumber() {
            return 254.52;
        }
    }

    @AllArgsConstructor
    static class NewInterface implements ModernInterface {
        private OldInterfaceClass oldInterfaceClass;

        @Override
        public int getNumber() {
            return (int) oldInterfaceClass.getOldNumber();
        }
    }

    @Test
    public void test() {
        // When
        var oldInterfaceClass = spy(new OldInterfaceClass());
        ModernInterface newInterface = spy(new NewInterface(oldInterfaceClass));

        var actual = newInterface.getNumber();

        assertEquals(254, actual);
        verify(oldInterfaceClass, times(1)).getOldNumber();
        verify(newInterface, times(1)).getNumber();
    }
}
