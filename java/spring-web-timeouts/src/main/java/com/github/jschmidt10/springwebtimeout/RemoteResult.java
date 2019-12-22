package com.github.jschmidt10.springwebtimeout;

public class RemoteResult {
    private final int result;
    private final long responseTime;

    public RemoteResult(int result, long responseTime) {
        this.result = result;
        this.responseTime = responseTime;
    }

    public int getResult() {
        return result;
    }

    public long getResponseTime() {
        return responseTime;
    }
}
