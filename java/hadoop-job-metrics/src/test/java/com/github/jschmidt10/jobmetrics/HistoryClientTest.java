package com.github.jschmidt10.jobmetrics;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HistoryClientTest {

    private static final String JOB_ID = "job_1234567890000_12345";

    private static DummyHistoryServer server;

    @BeforeClass
    public static void setupEmbeddedWebserver() throws Exception {
        server = DummyHistoryServer.create("jobHistoryResponse.json");
    }

    @Test
    public void shouldFetchHistory() throws IOException {
        HistoryClient historyClient = new HistoryClient("localhost", server.getPort());
        JobHistory history = historyClient.fetch(JOB_ID);

        assertThat(history.elapsedTime(), is(135577L));
        assertThat(history.avgReduceTime(), is(124961L));
        assertThat(history.avgMapTime(), is(2638L));
    }

    @AfterClass
    public static void shutdownEmbeddedWebserver() throws Exception {
        server.close();
    }
}
