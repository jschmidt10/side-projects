package com.github.jschmidt10.jobmetrics;

import net.savantly.graphite.CarbonMetric;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collection;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HistoryMetricsGathererTest {

    private static final String JOB_ID = "job_1234567890000_12345";

    private static DummyHistoryServer server;

    @BeforeClass
    public static void setupEmbeddedWebserver() throws Exception {
        server = DummyHistoryServer.create("jobHistoryResponse.json");
    }

    @Test
    public void shouldFetchJobMetrics() {
        HistoryMetricsGatherer gatherer = new HistoryMetricsGatherer("prefix", server.getUrl());
        Collection<CarbonMetric> metrics = gatherer.gather(JOB_ID);

        assertMetric(metrics, "prefix.elapsedTime", 135577); // finishedTime - startTime
        assertMetric(metrics, "prefix.avgReduceTime", 124961);
        assertMetric(metrics, "prefix.avgMapTime", 2638);
    }

    private void assertMetric(Collection<CarbonMetric> metrics, String metricName, long value) {
        Optional<CarbonMetric> metric = metrics.stream().filter(m -> m.getMetricName().equals(metricName)).findAny();

        assertThat(metric.isPresent(), is(true));
        assertThat(metric.get().getValue(), is(String.valueOf(value)));
    }

    @AfterClass
    public static void shutdownEmbeddedWebserver() throws Exception {
        server.close();
    }
}
