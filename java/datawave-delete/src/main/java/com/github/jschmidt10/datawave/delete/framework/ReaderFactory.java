package com.github.jschmidt10.datawave.delete.framework;

import java.io.IOException;

public interface ReaderFactory<IN>  {
    Reader<IN> newReader() throws IOException;
}
