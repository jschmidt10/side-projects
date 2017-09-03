package com.github.jschmidt10.jobmetrics;

import com.fasterxml.jackson.databind.JsonNode;
import net.savantly.graphite.CarbonMetric;

/**
 * Extracts a single metric from the JSON node representing the history response.
 */
public interface Extractor {

    /**
     * Extract a metric.
     *
     * @param prefix     the prefix to put on the metric name before sending to Carbon
     * @param metricTime the time to use for the generated metric
     * @param node       the json node representing the job stats
     * @return metric
     */
    CarbonMetric extract(String prefix, long metricTime, JsonNode node);
}
