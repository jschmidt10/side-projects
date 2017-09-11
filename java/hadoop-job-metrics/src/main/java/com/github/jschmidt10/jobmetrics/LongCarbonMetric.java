package com.github.jschmidt10.jobmetrics;

import net.savantly.graphite.impl.SimpleCarbonMetric;

/**
 * A wrapper for a CarbonMetric that will take a long value and convert it to a String.
 */
public class LongCarbonMetric extends SimpleCarbonMetric {

    public LongCarbonMetric(String name, long value, long epoch) {
        super(name, String.valueOf(value), epoch);
    }
}
