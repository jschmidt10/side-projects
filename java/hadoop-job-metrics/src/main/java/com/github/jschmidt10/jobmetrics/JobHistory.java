package com.github.jschmidt10.jobmetrics;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.InputStream;

/**
 * The job history of a particular Hadoop job.
 */
@AllArgsConstructor
public class JobHistory {

    private final static ObjectMapper mapper = new ObjectMapper();

    /**
     * Creates a new JobHistory from an InputStream containing JSON.
     *
     * @param input
     * @return job history
     */
    public static JobHistory fromStream(InputStream input) throws IOException {
        return new JobHistory(mapper.readValue(input, JsonNode.class).get("job"));
    }

    private final JsonNode jsonNode;

    /**
     * Gets the elapsed time of this job.
     *
     * @return elapsed time
     */
    public long elapsedTime() {
        long startTime = fieldAsLong("startTime");
        return finishedTime() - startTime;
    }

    /**
     * Gets the time this job finished.
     *
     * @return finish time
     */
    public long finishedTime() {
        return fieldAsLong("finishTime");
    }

    /**
     * Gets the average map time of the job.
     *
     * @return average map time
     */
    public long avgMapTime() {
        return fieldAsLong("avgMapTime");
    }

    /**
     * Gets the average reduce time of the job.
     *
     * @return average reduce time
     */
    public long avgReduceTime() {
        return fieldAsLong("avgReduceTime");
    }

    private long fieldAsLong(String name) {
        return jsonNode.get(name).asLong();
    }
}
