package com.github.jschmidt10.datawave.delete.framework;

import java.io.IOException;

public interface Writer<V> extends AutoCloseable {
    void write(MultiPair<V> multiPair) throws IOException;
}
