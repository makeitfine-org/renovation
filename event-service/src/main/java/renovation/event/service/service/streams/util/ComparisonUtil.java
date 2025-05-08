/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.event.service.service.streams.util;

public class ComparisonUtil {
    private final String comparison;
    private final double number;

    public ComparisonUtil(String comparison, double number) {
        this.comparison = comparison;
        this.number = number;
    }

    public boolean compare(double price) {
        int result = Double.compare(price, number);
        switch (comparison) {
            case "<":
                return result < 0;
            case "<=":
                return result <= 0;
            case ">":
                return result > 0;
            case ">=":
                return result >= 0;
            case "==":
                return result == 0;
            case "!=":
                return result != 0;
            default:
                throw new IllegalArgumentException("Invalid comparison operator: " + comparison);
        }
    }
}
