package com.github.jschmidt10.springwebtimeout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class RemoteTask implements Callable<Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    private long timeoutMillis;

    public RemoteTask(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    @Override
    public Integer call() {
        try {
            TimeUnit.MILLISECONDS.sleep(timeoutMillis);
            return 1;
        }
        catch (InterruptedException e) {
            LOGGER.warn("RemoteFetch was interrupted.", e);
            return -1;
        }
    }
}