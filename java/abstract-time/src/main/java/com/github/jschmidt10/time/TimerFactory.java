package com.github.jschmidt10.time;

/**
 * A factory for creating JVM-wide timers. Each type of timer will be reused for subsequent calls.
 */
public class TimerFactory {

    private static Timer secondTimer;
    private static Timer millisecondTimer;
    private static TestTimer testTimer;

    /**
     * Creates a second-precision timer.
     *
     * @return timer
     */
    public static Timer secondTimer() {
        if (secondTimer == null) {
            secondTimer = new ScheduledTimer(1000L);
        }
        return secondTimer;
    }

    /**
     * Creates a millisecondTimer-precision timer.
     *
     * @return timer
     */
    public static Timer millisecondTimer() {
        if (millisecondTimer == null) {
            millisecondTimer = new MillisecondTimer();
        }
        return millisecondTimer;
    }

    /**
     * Creates a test timer.
     *
     * @return timer
     */
    public static TestTimer testTimer() {
        return testTimer(false);
    }

    /**
     * Creates a test timer.
     *
     * @param recreate Should a new timer be recreated
     * @return timer
     */
    public static TestTimer testTimer(boolean recreate) {
        if (testTimer == null || recreate) {
            testTimer = new TestTimer();
        }
        return testTimer;
    }
}
