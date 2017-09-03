package com.github.jschmidt10.jobmetrics;

import com.fasterxml.jackson.databind.JsonNode;
import net.savantly.graphite.CarbonMetric;
import net.savantly.graphite.impl.SimpleCarbonMetric;

/**
 * Generic extractor for a simple long-based attribute.
 */
public class FieldExtractor implements Extractor {

    private final String suffix;
    private final String field;

    public FieldExtractor(String suffix, String field) {
        this.suffix = suffix;
        this.field = field;
    }

    @Override
    public CarbonMetric extract(String prefix, long metricTime, JsonNode node) {
        long value = node.get(field).asLong();
        return new SimpleCarbonMetric(prefix + suffix, String.valueOf(value), metricTime);
    }
}
