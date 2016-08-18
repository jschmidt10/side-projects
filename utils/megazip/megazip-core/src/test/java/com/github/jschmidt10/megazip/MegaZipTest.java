package com.github.jschmidt10.megazip;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class MegaZipTest {

    private static final String INPUT_FILE = "/sample.txt";

    private int blockSize = 1024;
    private int maxFailedGuesses = (int) Math.pow(2, 20);

    private InputStream inputFile;
    private String expectedFileContents;

    private Zip zipper;
    private Unzip unzipper;

    @Before
    public void setup() throws Exception {
        URI uri = this.getClass().getResource(INPUT_FILE).toURI();
        Path p = Paths.get(uri);

        inputFile = Files.newInputStream(p);
        expectedFileContents = readAsString(p);

        zipper = new Zip(blockSize);
        unzipper = new Unzip(maxFailedGuesses);
    }

    @Test
    public void shouldCompressAndDecompress() throws Exception {
        ByteArrayOutputStream compressedBytes = new ByteArrayOutputStream();
        ByteArrayOutputStream decompressedBytes = new ByteArrayOutputStream();

        zipper.zip(inputFile, compressedBytes);
        unzipper.unzip(bais(compressedBytes), decompressedBytes);

        String actual = new String(decompressedBytes.toByteArray());

        assertThat(actual, is(expectedFileContents));
    }

    private InputStream bais(ByteArrayOutputStream compressedBytes) {
        return new ByteArrayInputStream(compressedBytes.toByteArray());
    }

    private String readAsString(Path p) throws IOException {
        StringBuilder sb = new StringBuilder();

        try (Scanner scanner = new Scanner(Files.newInputStream(p))) {
            sb.append(scanner.nextLine());
        }

        return sb.toString();
    }
}
