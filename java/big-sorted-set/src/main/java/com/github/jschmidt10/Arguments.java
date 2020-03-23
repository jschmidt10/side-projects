package com.github.jschmidt10;

/**
 * Argument verification functions.
 */
public final class Arguments {

    private Arguments() {}

    /**
     * Verifies that an integer is greater than zero.
     *
     * @param value An integer value
     * @return The integer
     * @throws IllegalArgumentException if the value is less than or equal to zero
     */
    public static int greaterThanZero(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Must be greater than 0");
        }

        return value;
    }
}
