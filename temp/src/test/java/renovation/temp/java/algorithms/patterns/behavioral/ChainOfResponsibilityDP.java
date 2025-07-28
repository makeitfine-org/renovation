/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.temp.java.algorithms.patterns.behavioral;

import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ChainOfResponsibilityDP {

    static abstract class Logger {
        private static org.slf4j.Logger LOG = LoggerFactory.getLogger(Logger.class);

        public static final int ERROR = 1;
        public static final int NOTICE = 2;
        public static final int DEBUG = 3;

        protected int mask;

        public Logger(int mask) {
            this.mask = mask;
        }

        protected Logger nextLogger;

        void setNextLogger(Logger logger) {
            nextLogger = logger;
        }

        void log(String message, int level) {
            if (level <= mask) {
                message(message);
            }
            if (nextLogger != null) {
                nextLogger.log(message, level);
            }
        }

        public void message(String message) {
            LOG.info("{}: {}", mask, message);
        }
    }

    static class DedubLogger extends Logger {
        public DedubLogger() {
            super(DEBUG);
        }
    }

    static class NoticeLogger extends Logger {
        public NoticeLogger() {
            super(NOTICE);
        }
    }

    static class ErrorLogger extends Logger {
        public ErrorLogger() {
            super(ERROR);
        }
    }

    @Test
    public void test() {

        // When
        var errLogger = spy(new ErrorLogger());
        var noticeLogger = spy(new NoticeLogger());
        var debugLogger = spy(new DedubLogger());

        debugLogger.setNextLogger(noticeLogger);
        noticeLogger.setNextLogger(errLogger);

        debugLogger.log("error message", Logger.ERROR);
        debugLogger.log("notice message", Logger.NOTICE);
        debugLogger.log("debug message", Logger.DEBUG);

        // Then
        verify(debugLogger, times(3)).log(anyString(), anyInt());
        verify(debugLogger, times(3)).message(anyString());

        verify(noticeLogger, times(3)).log(anyString(), anyInt());
        verify(noticeLogger, times(2)).message(anyString());

        verify(errLogger, times(3)).log(anyString(), anyInt());
        verify(errLogger, times(1)).message(anyString());
    }
}
