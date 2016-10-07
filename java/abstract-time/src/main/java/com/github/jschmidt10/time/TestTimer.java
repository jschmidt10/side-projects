package com.github.jschmidt10.time;

/**
 * A timer used for testing. Can be progressed manually.
 */
public class TestTimer implements Timer {

    private long currentTime;

    public TestTimer() {
        this(0L);
    }

    public TestTimer(long startTime) {
        this.currentTime = startTime;
    }

    /**
     * Progress the timer forward by the given amount.
     *
     * @param delta
     */
    public void tick(long delta) {
        this.currentTime += delta;
    }

    @Override
    public long getTime() {
        return currentTime;
    }

    @Override
    public void close() throws Exception {

    }
}
