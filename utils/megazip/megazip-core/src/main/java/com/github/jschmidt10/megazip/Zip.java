package com.github.jschmidt10.megazip;

import java.io.*;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * The Megazip Compression algorithm.
 */
public class Zip {

    private final int blockSize;
    private final int formatVersion;

    public Zip(int blockSize) {
        this(MegaZip.CURRENT_VERSION, blockSize);
    }

    public Zip(int formatVersion, int blockSize) {
        this.formatVersion = formatVersion;
        this.blockSize = blockSize;
    }

    /**
     * Compresses the given input stream into to given output stream.
     *
     * @param istream input stream to read from
     * @param ostream output stream to write to
     * @throws NoSuchAlgorithmException if the desired hash algorithm is not available
     * @throws DigestException          on hash errors
     * @throws IOException              on read/write errors
     */
    public void zip(InputStream istream, OutputStream ostream) throws NoSuchAlgorithmException, DigestException, IOException {
        byte[] inputBuf = new byte[blockSize];
        byte[] outputBuf = new byte[MegaZip.HASH_LEN];
        MessageDigest md = MessageDigest.getInstance(MegaZip.HASH_ALG);

        int numRead;
        int hashLen;

        DataOutput output = new DataOutputStream(ostream);

        output.write(MegaZip.MAGIC);
        output.writeInt(formatVersion);
        output.writeInt(blockSize);

        while ((numRead = istream.read(inputBuf)) > 0) {
            md.update(inputBuf, 0, numRead);
            hashLen = md.digest(outputBuf, 0, MegaZip.HASH_LEN);

            if (numRead == blockSize) {
                output.writeByte(MegaZip.FULL_BLOCK);
            } else {
                output.writeByte(MegaZip.PARTIAL_BLOCK);
                output.writeInt(numRead);
            }

            output.write(outputBuf, 0, hashLen);
        }
    }
}
