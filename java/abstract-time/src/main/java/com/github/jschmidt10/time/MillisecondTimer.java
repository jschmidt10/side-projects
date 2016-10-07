package com.github.jschmidt10.time;

/**
 * A millisecond precision timer.
 */
public class MillisecondTimer implements Timer {

    @Override
    public long getTime() {
        return System.currentTimeMillis();
    }

    @Override
    public void close() throws Exception {
    }
}
