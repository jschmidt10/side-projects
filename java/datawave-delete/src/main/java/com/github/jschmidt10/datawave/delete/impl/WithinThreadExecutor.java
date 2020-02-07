package com.github.jschmidt10.datawave.delete.impl;

import com.github.jschmidt10.datawave.delete.framework.*;

public class WithinThreadExecutor implements StageExecutor {
    @Override
    public <IN, OUT> void execute(Stage<IN, OUT> stage, ReaderFactory<IN> readerFactory, WriterFactory<OUT> writerFactory) throws Exception {
        try (Reader<IN> reader = readerFactory.newReader()) {
            try (Writer<OUT> writer = writerFactory.newWriter()) {
                stage.process(reader.iterator(), writer);
            }
        }
    }
}
