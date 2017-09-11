package com.github.jschmidt10.jobmetrics;

import net.savantly.graphite.GraphiteClient;

/**
 * Wrapper around a GraphiteClient that uses an optional static metrics prefix and accepts long valued metrics instead of Strings.
 */
public class JobMetricsGraphiteClient {

    private final GraphiteClient graphiteClient;
    private final String metricsPrefix;

    public JobMetricsGraphiteClient(GraphiteClient graphiteClient, String metricsPrefix) {
        this.graphiteClient = graphiteClient;
        this.metricsPrefix = metricsPrefix;
    }

    /**
     * Sends a metric to Carbon.
     *
     * @param metricName
     * @param value
     * @param epoch
     */
    public void sendMetric(String metricName, long value, long epoch) {
        graphiteClient.saveCarbonMetrics(new LongCarbonMetric(metricsPrefix + metricName, value, epoch));
    }
}
