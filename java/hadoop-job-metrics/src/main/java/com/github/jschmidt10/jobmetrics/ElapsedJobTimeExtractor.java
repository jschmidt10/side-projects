package com.github.jschmidt10.jobmetrics;

import com.fasterxml.jackson.databind.JsonNode;
import net.savantly.graphite.CarbonMetric;
import net.savantly.graphite.impl.SimpleCarbonMetric;

/**
 * Extracts the elapsed job time from the history response.
 */
public class ElapsedJobTimeExtractor implements Extractor {

    private static final String SUFFIX = ".elapsedTime";

    @Override
    public CarbonMetric extract(String prefix, long metricTime, JsonNode node) {
        long startTime = node.get("startTime").asLong();
        long endTime = node.get("finishTime").asLong();

        long elapsedTime = endTime - startTime;

        return new SimpleCarbonMetric(prefix + SUFFIX, String.valueOf(elapsedTime), metricTime);
    }
}
