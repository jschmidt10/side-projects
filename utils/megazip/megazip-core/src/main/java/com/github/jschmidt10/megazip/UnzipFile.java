package com.github.jschmidt10.megazip;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * The main entry point for file zip/unzip.
 */
public class UnzipFile {

    /**
     * Prints the usage for this utility.
     */
    private static void usage() {
        System.out.println("UnzipFile expects arguments: <input file> <output file> <max failed guesses>");
        System.out.println();
        System.out.println("<input file>                Megazip compressed file to decompress.");
        System.out.println("<output file>               The file to create from the decompression.");
        System.out.println("<max failed guesses>        Max number of failed guess for a block before failing.");
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
        Verify.isLong(args[2]);
    }

    /**
     * Entry point for the utility.
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        try {
            verifyArgs(args);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            usage();
            System.exit(0);
        }

        String inputFile = args[0];
        String outputFile = args[1];
        long maxNumGuesses = Long.parseLong(args[2]);

        Unzip unzipper = new Unzip(MegaZip.CURRENT_VERSION, maxNumGuesses);

        try (FileInputStream fis = new FileInputStream(inputFile)) {
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                unzipper.unzip(fis, fos);
            }
        }

        System.out.println("Wrote file " + outputFile);
    }
}
