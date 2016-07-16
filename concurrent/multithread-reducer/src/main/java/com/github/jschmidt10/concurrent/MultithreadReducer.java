package com.github.jschmidt10.concurrent;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The class is used to have each thread locally collect and reduce key value
 * pairs. Finally, at the end, the reduce function will be applied across all of
 * the threads.
 */
public abstract class MultithreadReducer<K, V> {

    private final Map<Long, Map<K, V>> allKeyValues;
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    public MultithreadReducer() {
        allKeyValues = new HashMap<>();
    }

    public MultithreadReducer(int numThreads) {
        allKeyValues = new HashMap<>(numThreads * 2);
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
        Map<K, V> localKeyValues = getOrCreateLocal();

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

    private Map<K, V> getOrCreateLocal() {
        Map<K, V> localKeyValues = null;

        localKeyValues = getLocalMap();

        if (localKeyValues == null) {
            localKeyValues = createLocalMap();
        }

        return localKeyValues;
    }

    private Map<K, V> createLocalMap() {
        Map<K, V> localKeyValues = new TreeMap<K, V>();
        writeLock.lock();
        try {
            allKeyValues.put(Thread.currentThread().getId(), localKeyValues);
            return localKeyValues;
        } finally {
            writeLock.unlock();
        }
    }

    private Map<K, V> getLocalMap() {
        readLock.lock();
        try {
            Map<K, V> localKeyValues = allKeyValues.get(Thread.currentThread().getId());
            return localKeyValues;
        } finally {
            readLock.unlock();
        }
    }
}
