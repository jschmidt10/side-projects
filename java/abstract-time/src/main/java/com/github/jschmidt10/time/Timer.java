package com.github.jschmidt10.time;

/**
 * Maintains the program time.
 */
public interface Timer extends AutoCloseable {

    /**
     * Gets the current time.
     *
     * @return current time.
     */
    long getTime();
}
