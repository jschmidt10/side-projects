package com.github.jschmidt10.time;

import org.junit.Assert;
import org.junit.Test;

public class TimerFactoryTest {

    @Test
    public void secondTimerShouldMaintainToNearestSecond() throws Exception {
        Timer secondTimer = TimerFactory.secondTimer();
        long currentTime = System.currentTimeMillis();
        long currentTimeToSecond = currentTime - (currentTime % 1000L);

        Thread.sleep(500L);
        Assert.assertEquals(currentTimeToSecond, secondTimer.getTime());

        Thread.sleep(1000L);
        Assert.assertEquals(currentTimeToSecond + 1000L, secondTimer.getTime());
    }

    @Test
    public void testTimerShouldProgressAsSpecified() throws Exception {
        TestTimer testTimer = TimerFactory.testTimer();
        Assert.assertEquals(0L, testTimer.getTime());

        testTimer.tick(2000L);
        Assert.assertEquals(2000L, testTimer.getTime());
    }
}
