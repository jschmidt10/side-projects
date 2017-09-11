package com.github.jschmidt10.jobmetrics;

import lombok.AllArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

/**
 * The hadoop history server.
 */
@AllArgsConstructor
public class HistoryClient {

    private final static String URL_PATTERN = "http://%s:%d/ws/v1/history/mapreduce/jobs/%s";

    private final static CloseableHttpClient client = HttpClientBuilder.create().build();

    private final String host;
    private final int port;

    /**
     * Gathers job metrics bound for Carbon.
     *
     * @param jobId
     * @return metrics
     * @throws IOException when an error occurs executing the HTTP request or parsing the response.
     */
    public JobHistory fetch(String jobId) throws IOException {
        String url = String.format(URL_PATTERN, host, port, jobId);

        try (CloseableHttpResponse response = client.execute(new HttpGet(url))) {
            return JobHistory.fromStream(response.getEntity().getContent());
        }
    }
}
