package com.github.jschmidt10.megazip;

import java.io.*;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * The Megazip Decompression algorithm.
 */
public class Unzip {

    private static final int PRINT_CHARS_PER_BLOCK = 50;

    private final int formatVersion;
    private final long maxFailedGuesses;
    private final SecureRandom random;

    public Unzip(long maxFailedGuesses) {
        this(MegaZip.CURRENT_VERSION, maxFailedGuesses);
    }

    public Unzip(int formatVersion, long maxFailedGuesses) {
        this.formatVersion = formatVersion;
        this.maxFailedGuesses = maxFailedGuesses;
        this.random = new SecureRandom();
    }

    /**
     * Decompresses the given input stream to the given output stream.
     *
     * @param istream
     * @param ostream
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws DigestException
     */
    public void unzip(InputStream istream, OutputStream ostream) throws IOException, NoSuchAlgorithmException, DigestException {
        long guessPerChar = (maxFailedGuesses / PRINT_CHARS_PER_BLOCK);

        DataInputStream input = new DataInputStream(istream);
        verifyMagic(input);
        verifyVersion(input);

        int blockSize = input.readInt();
        byte[] outputBuf = new byte[blockSize];

        MessageDigest md = MessageDigest.getInstance(MegaZip.HASH_ALG);
        byte[] expectedHash = new byte[MegaZip.HASH_LEN];
        byte[] actualHash = new byte[MegaZip.HASH_LEN];

        long blocks = 0;

        while (input.available() > 0) {
            byte blockType = input.readByte();
            int contentSize = (blockType == MegaZip.FULL_BLOCK) ? blockSize : input.readInt();

            if (outputBuf.length != contentSize) {
                outputBuf = new byte[contentSize];
            }

            input.readFully(expectedHash);

            boolean found = false;
            int numGuesses = 0;

            System.out.print("Recreating [Block #" + blocks + "]\t\t");

            while (!found && numGuesses < maxFailedGuesses) {
                random.nextBytes(outputBuf);
                md.update(outputBuf);
                md.digest(actualHash, 0, actualHash.length);

                if ((numGuesses + 1) % guessPerChar == 0) {
                    System.out.print(".");
                }

                found = Arrays.equals(actualHash, expectedHash);
                numGuesses++;
            }

            if (!found) {
                throw new IOException("We tried " + maxFailedGuesses + " guesses but couldn't recover your file. Try again soon!");
            }

            System.out.println();

            ostream.write(outputBuf);
        }
    }

    private void verifyVersion(DataInputStream input) throws IOException {
        int parsedVersion = input.readInt();

        if (formatVersion != parsedVersion) {
            // TODO: Create exception
            throw new IllegalArgumentException("Version mismatch, we expected " + formatVersion + " but received " + parsedVersion);
        }
    }

    private void verifyMagic(DataInputStream input) throws IOException {
        byte[] magic = new byte[MegaZip.MAGIC.length];
        for (int i = 0; i < MegaZip.MAGIC.length; ++i) {
            magic[i] = input.readByte();
        }

        if (!Arrays.equals(magic, MegaZip.MAGIC)) {
            // TODO: Create exception
            throw new IllegalArgumentException("This does not appear to be a megazip file!");
        }
    }
}
