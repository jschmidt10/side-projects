package com.github.jschmidt10.megazip;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * The main entry point for file zip/unzip.
 */
public class ZipFile {

    /**
     * Prints the usage for this utility.
     */
    private static void usage() {
        System.out.println("ZipFile expects arguments: <input file> <output file> <block size>");
        System.out.println();
        System.out.println("<input file>        File to compress.");
        System.out.println("<output file>       The file to create from the compression.");
        System.out.println("<block size>        The block size to use for the compression.");
        System.out.println();
    }

    /**
     * Verifies the arguments passed in for the utility.
     *
     * @param args
     */
    private static void verifyArgs(String[] args) {
        Verify.numArgs(args, 3);
        Verify.fileReadable(args[0]);
        Verify.fileWritable(args[1]);
        Verify.isInt(args[2]);
    }

    /**
     * Entry point for this utility.
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        try {
            verifyArgs(args);
        } catch (IllegalArgumentException e) {
            usage();
            System.exit(0);
        }

        String inputFile = args[0];
        String outputFile = args[1];
        int blockSize = Integer.parseInt(args[2]);

        Zip zipper = new Zip(MegaZip.CURRENT_VERSION, blockSize);

        try (FileInputStream fis = new FileInputStream(inputFile)) {
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                zipper.zip(fis, fos);
            }
        }

        System.out.println("Wrote file " + outputFile);
    }
}
