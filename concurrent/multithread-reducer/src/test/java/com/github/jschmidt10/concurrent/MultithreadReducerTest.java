package com.github.jschmidt10.concurrent;

import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import static org.junit.Assert.*;

public class MultithreadReducerTest {

	@Test
	public void shouldPreserveConsistency() throws Exception {
		int numThreads = 4;
		int numKeys = 20000000;
		int maxKey = 200;

		ThreadPoolExecutor executor = new ThreadPoolExecutor(numThreads, numThreads, 0, TimeUnit.MICROSECONDS,
				new ArrayBlockingQueue<Runnable>(numThreads));
		executor.prestartAllCoreThreads();

		SummingReducer reducer = new SummingReducer();

		List<Worker> workers = new LinkedList<>();
		for (int i = 0; i < numThreads; ++i) {
			workers.add(new Worker(new KeyGenerator(numKeys, maxKey), reducer));
		}

		long start = System.currentTimeMillis();
		for (Worker worker : workers) {
			executor.submit(worker);
		}

		System.out.println(numThreads + " threads scheduled");

		executor.shutdown();
		executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

		Map<String, Long> global = reducer.reduceGlobal();

		long elapsed = System.currentTimeMillis() - start;

		long expected = (numThreads * numKeys);
		long actual = 0;

		for (Long value : global.values()) {
			actual += value;
		}

		System.out.println("ops = " + actual);
		System.out.println("numThreads = " + numThreads);
		System.out.println("runtime = " + elapsed);

		assertEquals(actual, expected);
	}

	private class Worker implements Runnable {

		private KeyGenerator generator;
		private SummingReducer reducer;

		public Worker(KeyGenerator generator, SummingReducer reducer) {
			this.generator = generator;
			this.reducer = reducer;
		}

		@Override
		public void run() {
			while (generator.hasNext()) {
				int next = generator.next();
				reducer.reduceLocal(String.valueOf(next), 1L);
			}
		}
	}

	private class KeyGenerator implements Iterator<Integer> {
		private int numKeys;
		private int maxKeys;
		private int curCount = 0;
		private Random random = new Random();

		public KeyGenerator(int numKeys, int maxKeys) {
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
