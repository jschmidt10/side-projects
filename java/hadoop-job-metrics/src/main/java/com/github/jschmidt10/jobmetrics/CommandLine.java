package com.github.jschmidt10.jobmetrics;

import com.beust.jcommander.Parameter;
import net.savantly.graphite.GraphiteClient;
import net.savantly.graphite.GraphiteClientFactory;

import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Handles running the JobMetrics application from the command line.
 */
public class CommandLine {

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

    /**
     * Creates a history client with the arguments given on the command line.
     *
     * @return history client
     */
    public HistoryClient historyClient() {
        return new HistoryClient(historyHost, historyPort);
    }

    /**
     * Creates a graphite client with the arguments given on the command line.
     *
     * @return graphite client
     */
    public JobMetricsGraphiteClient graphiteClient() throws SocketException, UnknownHostException {
        GraphiteClient graphiteClient = GraphiteClientFactory.defaultGraphiteClient(graphiteHost, graphitePort);
        return new JobMetricsGraphiteClient(graphiteClient, prefix);
    }

    /**
     * Gets the jobId that was given on the command line.
     *
     * @return jobId
     */
    public String jobId() {
        return jobId;
    }
}
