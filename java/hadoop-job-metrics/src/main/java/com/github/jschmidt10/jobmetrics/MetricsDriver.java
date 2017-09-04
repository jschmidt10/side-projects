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

    @Parameter(names = "--history-host", description = "The hostname running the Hadoop History server.", required = true)
    private String historyHost;

    @Parameter(names = "--history-port", description = "The port that Hadoop History server is listening on. (defaults to 8088)", required = false)
    private int historyPort = 8088;

    @Parameter(names = "--graphite-host", description = "The hostname running Graphite", required = true)
    private String graphiteHost;

    @Parameter(names = "--graphite-port", description = "The port that Graphite is listening on (defaults to 2003)", required = false)
    private int graphitePort = 2003;

    @Parameter(names = "--job", description = "The job id to record metrics for", required = true)
    private String jobId;

    @Parameter(names = "--metric-prefix", description = "The prefix to use for generated metrics", required = true)
    private String prefix;

    public void run() throws Exception {
        HistoryMetricsGatherer gatherer = new HistoryMetricsGatherer(historyHost, historyPort, prefix);
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
