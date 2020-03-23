package com.github.jschmidt10;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.SortedSet;

/**
 * A file of elements on local disk.
 * @param <T> The elemnt type
 */
public class SFile<T> {

    private final SFileSerializer<T> serializer;
    private final Path path;

    private int numCompactions;
    private int numElements;

    public SFile(SFileSerializer<T> serializer, Path path, int numCompactions, int numElements) {
        this.serializer = serializer;
        this.path = path;
        this.numCompactions = numCompactions;
        this.numElements = numElements;
    }

    /**
     * @return The number of compactions these elements have undergone
     */
    public int getNumCompactions() {
        return numCompactions;
    }

    /**
     * @return The number of elements
     */
    public int getNumElements() {
        return numElements;
    }

    /**
     * @return The file path
     */
    public Path getPath() {
        return path;
    }

    /**
     * Write out a buffer of elements to this file.
     * @param elements The elements to write.
     * @throws IOException If any errors occurring while writing
     */
    public void writeAll(SortedSet<T> elements) throws IOException {
        try (SFile<T>.Writer writer = newWriter()) {
            for (T element : elements) {
                writer.write(element);
            }
        }
    }

    /**
     * Creates a new SFileIterator over this file.
     * @return A new file iterator
     */
    public SFileIterator newFileIterator() {
        return new SFileIterator();
    }

    /**
     * Creates a new Writer to this file.
     * @return A new writer
     */
    public Writer newWriter() {
        return new Writer();
    }

    /**
     * Deletes this file on disk.
     * @throws IOException If an error occurs while deleting the file.
     */
    public void delete() throws IOException {
        Files.delete(path);
    }

    /**
     * Allows streaming writes to this ElementFile.
     */
    public class Writer implements AutoCloseable {

        private DataOutputStream dataOutput;
        private int numWritten = 0;

        private Writer() {
        }

        /**
         * Writes a new element to the file.
         * @param element The element to write
         * @throws IOException If any errors occur while writing to the file.
         */
        public void write(T element) throws IOException {
            if (dataOutput == null) {
                dataOutput = new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(path)));
                dataOutput.writeInt(numCompactions);
                dataOutput.writeInt(numElements);
            }
            serializer.write(element, dataOutput);
            numWritten++;

            if (numWritten > numElements) {
                throw new IllegalStateException("Expected " + numElements + " but received " + numWritten);
            }
        }

        @Override
        public void close() throws IOException {
            if (numWritten != numElements) {
                throw new IllegalStateException("Expected " + numElements + " but received " + numWritten);
            }

            dataOutput.close();
        }
    }

    /**
     * A reader over this file.
     */
    public class SFileIterator implements Iterator<T>, AutoCloseable {

        private DataInputStream dataInput;
        private int numRead;
        private T top;
        private boolean closed = false;

        private SFileIterator() {
        }

        @Override
        public boolean hasNext() {
            try {
                if (dataInput == null) {
                    dataInput = new DataInputStream(new BufferedInputStream(Files.newInputStream(path)));
                    numCompactions = dataInput.readInt();
                    numElements = dataInput.readInt();
                }
                if (top == null) {
                    if (numRead < numElements) {
                        top = serializer.read(dataInput);
                        numRead++;
                    }
                    else if (!closed){
                        dataInput.close();
                        closed = true;
                    }
                }
                return top != null;
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public T next() {
            T result = top;
            top = null;
            return result;
        }

        @Override
        public void close() throws IOException {
            if (!closed && dataInput != null) {
                dataInput.close();
            }
            closed = true;
        }
    }
}
