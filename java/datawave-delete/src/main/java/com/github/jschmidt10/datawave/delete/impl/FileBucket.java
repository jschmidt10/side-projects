package com.github.jschmidt10.datawave.delete.impl;

import com.github.jschmidt10.datawave.delete.framework.*;
import com.github.jschmidt10.datawave.delete.framework.Reader;
import com.github.jschmidt10.datawave.delete.framework.Writer;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class FileBucket<V> implements ReaderFactory<V>, WriterFactory<V>, Serializable {

    private final String filename;
    private Collection<MultiPair<V>> writtenPairs = new ArrayList<>();

    public FileBucket() {
        this(newRandomFile());
    }

    public FileBucket(String filename) {
        this.filename = filename;
    }

    private static String newRandomFile() {
        try {
            return File.createTempFile("FileBucket", "").getAbsolutePath();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Reader<V> newReader() throws IOException {
        return new Reader<>() {
            private FileInputStream fis = new FileInputStream(filename);

            @Override
            public Iterator<MultiPair<V>> iterator() {
                try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename))) {
                    writtenPairs = (Collection<MultiPair<V>>) inputStream.readObject();
                    return writtenPairs.iterator();

                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void close() throws Exception {
                fis.close();
            }
        };
    }

    @Override
    public Writer<V> newWriter() {
        return new Writer<>() {
            @Override
            public void write(MultiPair<V> multiPair) {
                writtenPairs.add(multiPair);
            }

            @Override
            public void close() throws Exception {
                try(ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
                    outputStream.writeObject(writtenPairs);
                }
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FileBucket{filename=").append(filename).append(",multiPairs=[");

        try (Reader<V> reader = newReader()){
            for (MultiPair<V> multiPair : reader) {
                sb.append(multiPair).append(',');
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        sb.append("]}");

        return sb.toString();
    }
}
