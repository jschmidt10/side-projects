package com.github.jschmidt10.peg;

/**
 * Utilities for ensuring state in the program.
 */
public class Ensure {

    private Ensure() {
    }

    /**
     * Checks a condition and fails if it is not met.
     *
     * @param condition
     * @param message
     */
    public static void ensure(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }
}
