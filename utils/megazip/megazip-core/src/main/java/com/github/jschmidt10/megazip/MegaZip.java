package com.github.jschmidt10.megazip;

/**
 * Megazip configurations.
 */
public class MegaZip {

    public static final int CURRENT_VERSION = 0;
    public static final String HASH_ALG = "MD5";
    public static final int HASH_LEN = 16;
    public static final byte[] MAGIC = "MZ".getBytes();

    public static final byte FULL_BLOCK = 0x00;
    public static final byte PARTIAL_BLOCK = 0x01;

}
