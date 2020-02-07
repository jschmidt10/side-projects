package com.github.jschmidt10.datawave.delete.framework;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

public class MultiPair<V> implements Serializable {
    private final DeleteRequest request;
    private final Collection<V> values;

    public MultiPair(DeleteRequest request, V value) {
        this(request, Collections.singleton(value));
    }

    public MultiPair(DeleteRequest request, Collection<V> values) {
        this.request = request;
        this.values = values;
    }

    public DeleteRequest getRequest() {
        return request;
    }

    public Collection<V> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "MultiPair{" +
                "request=" + request +
                ", values=" + values +
                '}';
    }
}
