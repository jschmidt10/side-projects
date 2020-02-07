package com.github.jschmidt10.datawave.delete.framework;

public interface StageExecutor {
    <IN,OUT> void execute(Stage<IN,OUT> stage, ReaderFactory<IN> readerFactory, WriterFactory<OUT> writerFactory) throws Exception;
}
