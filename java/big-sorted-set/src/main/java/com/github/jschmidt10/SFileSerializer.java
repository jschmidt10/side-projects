package com.github.jschmidt10;

import java.io.*;

/**
 * Serializes and deserializes elements.
 * @param <T>
 */
public interface SFileSerializer<T> {

    /**
     * Write an element to the given output stream.
     * @param element The element
     * @param dataOutput The output stream to write to
     * @throws IOException If any errors occur during serialization or writing to the dataOutput
     */
    void write(T element, DataOutput dataOutput) throws IOException;

    /**
     * Reads an element from the given input stream.
     * @param dataInput The input stream to read from
     * @return The next element
     * @throws IOException If any errors occur during deserialization or reading the dataInput
     */
    T read(DataInput dataInput) throws IOException;
}
