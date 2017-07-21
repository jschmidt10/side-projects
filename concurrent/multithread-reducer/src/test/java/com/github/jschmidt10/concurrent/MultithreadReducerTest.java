package com.github.jschmidt10.concurrent;

import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class MultithreadReducerTest {

    @Test
    public void shouldPreserveConsistency() throws Exception {
        int numThreads = Runtime.getRuntime().availableProcessors();
        int numKeys = 20000000;
        int maxKey = 200;

        ThreadPoolExecutor executor = new ThreadPoolExecutor(numThreads, numThreads, 0, TimeUnit.MICROSECONDS,
                new ArrayBlockingQueue<>(numThreads));
        executor.prestartAllCoreThreads();

        SummingReducer reducer = new SummingReducer();
        ConcurrentHashMap<String, Long> globalCounts = new ConcurrentHashMap<>(2 ^ 20, 0.75f, numThreads);

        List<Worker> workers = new LinkedList<>();
        for (int i = 0; i < numThreads; ++i) {
            workers.add(new Worker(new KeyGenerator(numKeys, maxKey), reducer, globalCounts));
        }

        long start = System.currentTimeMillis();
        for (Worker worker : workers) {
            executor.submit(worker);
        }

        System.out.println(numThreads + " threads scheduled");

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

        long elapsed = System.currentTimeMillis() - start;

        long expected = (numThreads * numKeys);
        long actual = 0;

        for (Long value : globalCounts.values()) {
            actual += value;
        }

        System.out.println("ops = " + actual);
        System.out.println("numThreads = " + numThreads);
        System.out.println("runtime = " + elapsed);

        assertEquals(actual, expected);
    }

    private class Worker implements Runnable {

        private final KeyGenerator generator;
        private final SummingReducer reducer;
        private final ConcurrentHashMap<String, Long> globalCounts;

        Worker(KeyGenerator generator, SummingReducer reducer, ConcurrentHashMap<String, Long> globalCounts) {
            this.generator = generator;
            this.reducer = reducer;
            this.globalCounts = globalCounts;
        }

        @Override
        public void run() {
            while (generator.hasNext()) {
                int next = generator.next();
                reducer.reduceLocal(String.valueOf(next), 1L);
            }
            reducer.reduceGlobal(globalCounts);
        }
    }

    private class KeyGenerator implements Iterator<Integer> {
        private int numKeys;
        private int maxKeys;
        private int curCount = 0;
        private Random random = new Random();

        KeyGenerator(int numKeys, int maxKeys) {
            this.numKeys = numKeys;
            this.maxKeys = maxKeys;
        }

        @Override
        public boolean hasNext() {
            return curCount < numKeys;
        }

        @Override
        public Integer next() {
            curCount++;
            return random.nextInt(maxKeys);
        }
    }
}
