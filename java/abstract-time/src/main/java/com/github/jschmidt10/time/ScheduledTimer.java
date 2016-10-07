package com.github.jschmidt10.time;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A {@link Runnable} that tracks time to the given precision.
 */
public class ScheduledTimer implements Timer {

    private final long precision;
    private final ScheduledExecutorService executor;
    private volatile long currentTime;

    public ScheduledTimer(long precisionMillis) {
        this.precision = precisionMillis;
        this.executor = Executors.newScheduledThreadPool(1);
        this.executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                long fullTime = System.currentTimeMillis();
                currentTime = fullTime - (fullTime % precision);
            }
        }, 0L, precisionMillis, TimeUnit.MILLISECONDS);
    }

    @Override
    public long getTime() {
        return currentTime;
    }

    @Override
    public void close() throws Exception {
        executor.shutdownNow();
    }
}
