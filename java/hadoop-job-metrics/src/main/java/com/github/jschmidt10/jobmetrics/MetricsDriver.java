package com.github.jschmidt10.jobmetrics;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import net.savantly.graphite.CarbonMetric;
import net.savantly.graphite.GraphiteClient;
import net.savantly.graphite.GraphiteClientFactory;

import java.util.Collection;

/**
 * Command line runner for the history metrics utility.
 */
public class MetricsDriver {

    @Parameter(names = "--history", description = "The Hadoop History Server's REST url (e.g. 'http://localhost:8088/ws/v1/history/mapreduce/jobs/')", required = true)
    private String historyUrl;

    @Parameter(names = "--graphiteHost", description = "The hostname running Graphite", required = true)
    private String graphiteHost;

    @Parameter(names = "--graphitePort", description = "The port that Graphite is listening on (defaults to 2003)", required = false)
    private int graphitePort = 2003;

    @Parameter(names = "--job", description = "The job id to record metrics for", required = true)
    private String jobId;

    @Parameter(names = "--metricPrefix", description = "The prefix to use for generated metrics", required = true)
    private String prefix;

    public void run() throws Exception {
        HistoryMetricsGatherer gatherer = new HistoryMetricsGatherer(prefix, historyUrl);
        GraphiteClient graphiteClient = GraphiteClientFactory.defaultGraphiteClient(graphiteHost, graphitePort);

        Collection<CarbonMetric> metrics = null;

        try {
            metrics = gatherer.gather(jobId);
        } catch (Exception e) {
            System.err.println("Failed to collect job metrics from the history server.");
            e.printStackTrace();
            System.exit(1);
        }

        try {
            graphiteClient.saveCarbonMetrics(metrics);
        } catch (Exception e) {
            System.err.println("Failed to send metrics to graphite.");
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("Sent " + metrics.size() + " metrics to Graphite successfully.");
    }

    public static void main(String[] args) throws Exception {
        MetricsDriver driver = new MetricsDriver();
        JCommander jCommander = new JCommander(driver);

        try {
            jCommander.parse(args);
        } catch (Exception e) {
            jCommander.usage();
            System.exit(1);
        }

        driver.run();
    }
}
