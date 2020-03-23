package com.github.jschmidt10;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SFileTest {

    @Rule
    public TemporaryFolder tmpDir = new TemporaryFolder();

    private int numCompactions = 1;
    private int numElements = 10;
    private SFileSerializer<Integer> serializer = new IntSerializer();

    @Test
    public void shouldWriteAndReadFiles() throws IOException {
        SFile<Integer> sfile = newSFile();
        try (SFile<Integer>.Writer writer = sfile.newWriter()) {
            for (int i = 0; i < numElements; i++) {
                writer.write(i);
            }
        }

        try (SFile<Integer>.SFileIterator iterator = sfile.newFileIterator()) {
            int expected = 0;
            while (iterator.hasNext()) {
                assertThat(iterator.next(), is(expected++));
            }
            assertThat(numElements, is(expected));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowErrorOnWrongNumberOfElementsWritten() throws IOException {
        int numElements = 2;

        SFile<Integer> sFile = newSFile();
        try (SFile<Integer>.Writer writer = sFile.newWriter()) {
            for (int i = 0; i <= numElements; i++) {
                writer.write(i);
            }
        }
    }

    @Test
    public void shouldDeleteFile() throws IOException {
        SFile<Integer> sfile = newSFile();
        try (SFile<Integer>.Writer writer = sfile.newWriter()) {
            for (int i = 0; i < numElements; i++) {
                writer.write(i);
            }
        }

        assertThat(Files.exists(sfile.getPath()), is(true));

        sfile.delete();
        assertThat(Files.exists(sfile.getPath()), is(false));
    }

    private SFile<Integer> newSFile() throws IOException {
        Path path = tmpDir.newFile().toPath();
        return new SFile<>(serializer, path, numCompactions, numElements);
    }

    private static class IntSerializer implements SFileSerializer<Integer> {
        @Override
        public void write(Integer element, DataOutput dataOutput) throws IOException {
            dataOutput.writeInt(element);
        }

        @Override
        public Integer read(DataInput dataInput) throws IOException {
            return dataInput.readInt();
        }
    }
}
