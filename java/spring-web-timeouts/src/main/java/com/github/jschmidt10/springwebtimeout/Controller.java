package com.github.jschmidt10.springwebtimeout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@RestController
public class Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    private final long requestTimeout;
    private final ExecutorService workerPool;

    Controller(@Value("${web.request.timeout}") long requestTimeout) {
        this.requestTimeout = requestTimeout;
        this.workerPool = Executors.newCachedThreadPool(new CustomizableThreadFactory("remote-worker-pool-"));
    }

    @GetMapping("/duration/{requestDuration}")
    public ResponseEntity<?> fetchRemoteResult(@PathVariable long requestDuration) {
        Future<Integer> future = workerPool.submit(new RemoteTask(requestDuration));

        try {
            long startTime = System.currentTimeMillis();
            int result = future.get(requestTimeout, TimeUnit.MILLISECONDS);
            long finishTime = System.currentTimeMillis();

            return ResponseEntity.ok(new RemoteResult(result, finishTime - startTime));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("We're under heavy load at the moment.");
        }
    }
}
