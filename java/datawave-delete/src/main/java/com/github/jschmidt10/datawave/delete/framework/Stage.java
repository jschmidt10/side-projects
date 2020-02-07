package com.github.jschmidt10.datawave.delete.framework;

import java.util.Iterator;

public interface Stage<IN, OUT> {
    default String getName() {
        return getClass().getSimpleName();
    }

    void process(Iterator<MultiPair<IN>> reader, Writer<OUT> writer);
}
