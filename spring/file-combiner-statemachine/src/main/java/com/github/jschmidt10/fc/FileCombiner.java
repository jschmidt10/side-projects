package com.github.jschmidt10.fc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.statemachine.StateMachine;

import java.util.Random;

/**
 * Merges file content with a configured size or time cutoff.
 */
public class FileCombiner implements AutoCloseable {

    enum State {
        READY_FOR_DATA, CHECK_BUFFER, WRITING_DATA, ERROR
    }

    enum Event {
        NEW_FILE, BUFFER_FULL, BUFFER_NOT_FULL, WRITE_FINISHED, ERROR_OCCURRED
    }

    private final StateMachine<State, Event> sm;
    private final long fileSizeLimit = 256;
    private long combinedSize = 0;

    @Autowired
    public FileCombiner(StateMachine<FileCombiner.State, FileCombiner.Event> sm) {
        this.sm = sm;
        sm.start();
    }

    /**
     * Ingests a new file.
     *
     * @param nextFileSize
     */
    public void combine(long nextFileSize) {
        System.out.println("Ingesting file of " + nextFileSize + " MB");
        sm.sendEvent(Event.NEW_FILE);

        while (sm.getState().getId() != State.READY_FOR_DATA) {
            switch (sm.getState().getId()) {
                case CHECK_BUFFER:
                    if (combinedSize + nextFileSize > fileSizeLimit) {
                        sm.sendEvent(Event.BUFFER_FULL);
                    } else {
                        combinedSize += nextFileSize;
                        sm.sendEvent(Event.BUFFER_NOT_FULL);
                    }
                    break;
                case WRITING_DATA:
                    System.out.println("Writing new file of size: " + combinedSize + " MB");
                    combinedSize = 0;
                    sm.sendEvent(Event.WRITE_FINISHED);
                    break;
                case ERROR:
                    System.err.println("Error occurred during processing, stopping...");
                    sm.stop();
                    System.exit(1);
            }
        }
    }

    @Override
    public void close() throws Exception {
        try {
            sm.stop();
        } catch (Exception e) {
            System.err.println("Error closing StateMachine");
        }
    }

    public static void main(String[] args) throws Exception {
        int numIterations = 1000;
        long blockSize = 1; // 1 MB
        int maxBlocksPerFile = 64;

        Random random = new Random();

        ApplicationContext context = new AnnotationConfigApplicationContext(FileCombinerConfig.class);

        try (FileCombiner combiner = context.getBean(FileCombiner.class)) {
            for (int i = 0; i < numIterations; ++i) {
                long nextFile = random.nextInt(maxBlocksPerFile) * blockSize;
                combiner.combine(nextFile);
            }
        }
    }
}
