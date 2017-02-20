package com.github.jschmidt10.peg;

/**
 * Encoder/decoder for a jump.
 */
public class JumpEncoding {

    private static final int fromMask = 0xf00;
    private static final int overMask = 0x0f0;
    private static final int toMask = 0x00f;

    private JumpEncoding() {
    }

    /**
     * Encodes a jump as an int
     *
     * @param from
     * @param over
     * @param to
     * @return encoded jump
     */
    public static int encode(int from, int over, int to) {
        int encoded = 0;

        encoded += ((from << 8) & fromMask);
        encoded += ((over << 4) & overMask);
        encoded += ((to << 0) & toMask);

        return encoded;
    }

    /**
     * Decodes the from portion of the jump
     *
     * @param encoded
     * @return from
     */
    public static int decodeFrom(int encoded) {
        return (encoded & fromMask) >> 8;
    }

    /**
     * Decodes the over portion of the jump
     *
     * @param encoded
     * @return over
     */
    public static int decodeOver(int encoded) {
        return (encoded & overMask) >> 4;
    }

    /**
     * Decodes the to portion of the jump
     *
     * @param encoded
     * @return to
     */
    public static int decodeTo(int encoded) {
        return (encoded & toMask) >> 0;
    }
}
