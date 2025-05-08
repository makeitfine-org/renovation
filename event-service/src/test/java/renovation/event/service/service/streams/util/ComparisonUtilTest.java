/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.event.service.service.streams.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ComparisonUtilTest {

    @Test
    void testLessThan() {
        ComparisonUtil util = new ComparisonUtil("<", 100.0);
        assertTrue(util.compare(99.9));
        assertFalse(util.compare(100.0));
    }

    @Test
    void testLessThanOrEqual() {
        ComparisonUtil util = new ComparisonUtil("<=", 100.0);
        assertTrue(util.compare(99.9));
        assertTrue(util.compare(100.0));
        assertFalse(util.compare(100.1));
    }

    @Test
    void testGreaterThan() {
        ComparisonUtil util = new ComparisonUtil(">", 100.0);
        assertTrue(util.compare(100.1));
        assertFalse(util.compare(100.0));
    }

    @Test
    void testGreaterThanOrEqual() {
        ComparisonUtil util = new ComparisonUtil(">=", 100.0);
        assertTrue(util.compare(100.1));
        assertTrue(util.compare(100.0));
        assertFalse(util.compare(99.9));
    }

    @Test
    void testEqual() {
        ComparisonUtil util = new ComparisonUtil("==", 100.0);
        assertTrue(util.compare(100.0));
        assertFalse(util.compare(99.9));
    }

    @Test
    void testNotEqual() {
        ComparisonUtil util = new ComparisonUtil("!=", 100.0);
        assertTrue(util.compare(99.9));
        assertFalse(util.compare(100.0));
    }

    @Test
    void testInvalidOperator() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ComparisonUtil("invalid", 100.0).compare(100.0);
        });
        assertEquals("Invalid comparison operator: invalid", exception.getMessage());
    }
}
