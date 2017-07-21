package com.github.jschmidt10.concurrent;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The class is used to have each thread locally collect and reduce key value
 * pairs. Finally, at the end, the reduce function will be applied across all of
 * the threads.
 */
public abstract class MultithreadReducer<K, V> {

    private final ThreadLocal<Map<K, V>> localKeyValues;

    public MultithreadReducer() {
        this.localKeyValues = new ThreadLocal<>();
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
        if (localKeyValues.get() == null) {
            localKeyValues.set(new HashMap<>());
        }

        localKeyValues
                .get()
                .merge(key, value, (a, v) -> reduce(key, a, v));
    }

    /**
     * Reduces this thread's counts into the given global key/value map.
     *
     * @param globalKeyValues
     * @return globally reduced map
     */
    public void reduceGlobal(ConcurrentHashMap<K, V> globalKeyValues) {
        for (Entry<K, V> entry : localKeyValues.get().entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();

            globalKeyValues.merge(key, value, (a, v) -> reduce(key, a, v));
        }
    }
}
