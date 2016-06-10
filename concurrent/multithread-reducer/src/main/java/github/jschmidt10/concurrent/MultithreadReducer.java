package github.jschmidt10.concurrent;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * The class is used to have each thread locally collect and reduce key value
 * pairs. Finally, at the end, the reduce function will be applied across all of
 * the threads.
 */
public abstract class MultithreadReducer<K, V> {

	private final IdentityHashMap<Thread, Map<K, V>> allKeyValues;

	public MultithreadReducer() {
		allKeyValues = new IdentityHashMap<>();
	}

	public MultithreadReducer(int numThreads) {
		allKeyValues = new IdentityHashMap<>(numThreads);
	}

	/**
	 * Perform a reduce operation on the current accumulated value and the new
	 * value.
	 * 
	 * @param key
	 * @param accumulator
	 * @param value
	 * @return the reduced value
	 */
	public abstract V reduce(K key, V accumulator, V value);

	/**
	 * Reduces a new value into the local thread map.
	 * 
	 * @param key
	 * @param value
	 */
	public void reduceLocal(K key, V value) {
		Map<K, V> localKeyValues = getLocalKeyValues();

		V accumulator = localKeyValues.get(key);

		if (accumulator == null) {
			localKeyValues.put(key, value);
		} else {
			localKeyValues.put(key, reduce(key, accumulator, value));
		}
	}

	/**
	 * Reduces all of the thread's maps together.
	 * 
	 * @return globally reduced map
	 */
	public synchronized Map<K, V> reduceGlobal() {
		Map<K, V> global = new TreeMap<>();

		for (Map<K, V> threadMap : allKeyValues.values()) {
			for (Entry<K, V> entry : threadMap.entrySet()) {
				K key = entry.getKey();
				V accumulator = global.get(key);

				if (accumulator == null) {
					global.put(key, entry.getValue());
				} else {
					global.put(key, reduce(key, accumulator, entry.getValue()));
				}
			}
		}

		return global;
	}

	private Map<K, V> getLocalKeyValues() {
		Map<K, V> localKeyValues = allKeyValues.get(Thread.currentThread());

		if (localKeyValues == null) {
			localKeyValues = new TreeMap<K, V>();
			synchronized (allKeyValues) {
				allKeyValues.put(Thread.currentThread(), localKeyValues);
			}
		}

		return localKeyValues;
	}
}
