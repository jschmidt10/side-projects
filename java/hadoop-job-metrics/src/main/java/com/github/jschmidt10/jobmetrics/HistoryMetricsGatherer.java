package com.github.jschmidt10.jobmetrics;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import net.savantly.graphite.CarbonMetric;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Fetches elapsed time, avg reduce time, and avg map time.
 */
@AllArgsConstructor
public class HistoryMetricsGatherer {

    private final static ObjectMapper mapper = new ObjectMapper();
    private final static CloseableHttpClient client = HttpClientBuilder.create().build();

    private final static List<Extractor> extractors = Arrays.asList(new ElapsedJobTimeExtractor(), new FieldExtractor(".avgReduceTime", "avgReduceTime"), new FieldExtractor(".avgMapTime", "avgMapTime"));

    private final String host;
    private final int port;
    private final String prefix;


    /**
     * Gathers job metrics bound for Carbon.
     *
     * @param jobId
     * @return metrics
     */
    public Collection<CarbonMetric> gather(String jobId) {
        JsonNode rootNode = curlJobHistory(jobId);
        JsonNode jobNode = rootNode.get("job");

        long metricTime = jobNode.get("finishTime").asLong() / 1000;

        return extractors.stream().map(e -> e.extract(prefix, metricTime, jobNode)).collect(Collectors.toList());
    }

    private JsonNode curlJobHistory(String jobId) {
        String url = String.format("http://%s:%d/ws/v1/history/mapreduce/jobs/%s", host, port, jobId);

        try (CloseableHttpResponse response = client.execute(new HttpGet(url))) {
            return mapper.readValue(response.getEntity().getContent(), JsonNode.class);
        } catch (JsonParseException e) {
            throw new RuntimeException("Could not parse JSON response from " + url);
        } catch (IOException e) {
            throw new RuntimeException("Failed during HTTP GET to " + url);
        }
    }
}
