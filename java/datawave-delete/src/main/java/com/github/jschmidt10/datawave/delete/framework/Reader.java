package com.github.jschmidt10.datawave.delete.framework;

public interface Reader<V> extends Iterable<MultiPair<V>>, AutoCloseable {
}
