package com.github.jschmidt10.jobmetrics;

import com.beust.jcommander.JCommander;
import lombok.AllArgsConstructor;

/**
 * Command line runner for the history metrics utility.
 */
@AllArgsConstructor
public class JobMetrics {

    private final HistoryClient historyClient;
    private final JobMetricsGraphiteClient graphiteClient;
    private final String jobId;

    public void run() throws Exception {
        try {
            JobHistory jobHistory = historyClient.fetch(jobId);
            long metricTime = jobHistory.finishedTime();

            graphiteClient.sendMetric("elapsedTime", jobHistory.elapsedTime(), metricTime);
            graphiteClient.sendMetric("avgMapTime", jobHistory.avgMapTime(), metricTime);
            graphiteClient.sendMetric("avgReduceTime", jobHistory.avgReduceTime(), metricTime);

            System.out.println("Sent metrics to Graphite successfully.");
        } catch (Exception e) {
            System.err.println("Failed to send any metrics to Graphite");
            e.printStackTrace();
        }
    }

    /**
     * Command line entry point for the application.
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        CommandLine cl = new CommandLine();
        JCommander jCommander = new JCommander(cl);

        try {
            jCommander.parse(args);

            JobMetrics jobMetrics = new JobMetrics(cl.historyClient(), cl.graphiteClient(), cl.jobId());
            jobMetrics.run();

        } catch (Exception e) {
            jCommander.usage();
            System.exit(1);
        }
    }
}
