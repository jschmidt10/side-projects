package com.github.jschmidt10.datawave.delete.impl;

import com.github.jschmidt10.datawave.delete.framework.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class InMemoryBucket<V> implements ReaderFactory<V>, WriterFactory<V>, Serializable {

    private Collection<MultiPair<V>> multiPairs = new ArrayList<>();

    public Collection<MultiPair<V>> getMultiPairs() {
        return multiPairs;
    }

    @Override
    public Writer<V> newWriter() {
        return new Writer<>() {
            @Override
            public void close() throws Exception {

            }

            @Override
            public void write(MultiPair<V> multiPair) {
                multiPairs.add(multiPair);
            }
        };
    }

    @Override
    public Reader<V> newReader() {
        return new Reader<>() {
            @Override
            public void close() throws Exception {

            }

            @Override
            public Iterator<MultiPair<V>> iterator() {
                return multiPairs.iterator();
            }
        };
    }

    @Override
    public String toString() {
        return "InMemoryBucket{" +
                "multiPairs=" + multiPairs +
                '}';
    }
}
