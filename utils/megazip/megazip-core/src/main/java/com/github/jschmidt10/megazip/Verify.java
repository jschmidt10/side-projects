package com.github.jschmidt10.megazip;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utilties for verifying command line arguments.
 */
public class Verify {

    /**
     * Verifies that an argument is an integer.
     *
     * @param arg
     */
    public static void isInt(String arg) {
        try {
            Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Expected an integer but received: " + arg);
        }
    }

    /**
     * Verifies that an argument is a Long.
     *
     * @param arg
     */
    public static void isLong(String arg) {
        try {
            Long.parseLong(arg);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Expected a long but received: " + arg);
        }
    }

    /**
     * Verifies that a file is writable.
     *
     * @param file
     */
    public static void fileWritable(String file) {
        Path p = Paths.get(file);
        if (Files.exists(p)) {
            throw new IllegalArgumentException("Output file " + file + " already exists.");
        } else if (!Files.isWritable(p.getParent())) {
            throw new IllegalArgumentException("Output file " + file + " cannot be written to.");
        }
    }

    /**
     * Verifies that a file is readable.
     *
     * @param file
     */
    public static void fileReadable(String file) {
        Path p = Paths.get(file);
        if (!Files.exists(p) || !Files.isReadable(p)) {
            throw new IllegalArgumentException("Could not read input file: " + file);
        }
    }

    /**
     * Verifies the number of arguments.
     *
     * @param args
     * @param expectedLen
     */
    public static void numArgs(String[] args, int expectedLen) {
        if (args.length != expectedLen) {
            throw new IllegalArgumentException("Expected " + expectedLen + " arguments, but received " + args.length);
        }
    }

    private Verify() {
    }
}
