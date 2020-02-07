package com.github.jschmidt10.datawave.delete.framework;

import java.io.IOException;

public interface WriterFactory<OUT> {
    Writer<OUT> newWriter() throws IOException;
}
