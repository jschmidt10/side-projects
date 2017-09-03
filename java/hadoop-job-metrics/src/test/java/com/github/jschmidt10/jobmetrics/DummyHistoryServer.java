package com.github.jschmidt10.jobmetrics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * Embedded toy webserver for delivering a single job history response.
 */
@AllArgsConstructor
@Getter
public class DummyHistoryServer implements AutoCloseable {

    // We'll try five times to find a random unused port between 30000 and 40000.
    private final static int MIN_PORT = 30000;
    private final static int PORT_RANGE = 10000;
    private final static int MAX_PORT_TRIES = 5;

    private final static Random random = new Random();

    /**
     * Factory method to create a new history server.
     *
     * @param pathToResponse
     * @return server
     */
    public static DummyHistoryServer create(String pathToResponse) throws Exception {

        int port = 0;
        Server server = null;

        for (int i = 0; i < MAX_PORT_TRIES && server == null; i++) {
            port = randomPort();
            server = tryLaunch(port);
        }

        if (server == null) {
            throw new RuntimeException("We tried " + MAX_PORT_TRIES + " but we couldn't find an unused port.");
        }

        final String json = readResponse(pathToResponse);

        server.setHandler(new DefaultHandler() {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                response.getWriter().write(json);
                response.getWriter().flush();
            }
        });

        server.start();

        return new DummyHistoryServer(server, port);
    }

    /*
     * Generates a random port according to the configured rules.
     */
    private static int randomPort() {
        return random.nextInt(PORT_RANGE) + MIN_PORT;
    }

    /*
     * Reads the passed-in response file as a String.
     */
    private static String readResponse(String pathToResponse) {
        try {
            byte[] bytes = IOUtils.toByteArray(DummyHistoryServer.class.getClassLoader().getResourceAsStream(pathToResponse));
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read response file: " + pathToResponse + ", is it on the classpath?");
        }
    }

    /*
     * Tries to launch a server on the port.
     */
    private static Server tryLaunch(int port) {
        try {
            return new Server(port);
        } catch (Exception e) {
            return null;
        }
    }

    private final Server server;
    private final int port;

    public String getUrl() {
        return "http://localhost:" + port;
    }

    @Override
    public void close() throws Exception {
        server.stop();
    }
}
