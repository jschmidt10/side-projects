package com.github.jschmidt10.peg;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static com.github.jschmidt10.peg.Ensure.ensure;

/**
 * The peg game board. Spaces are numbered as so:
 * <p>
 * <pre>
 *            0
 *          1   2
 *        3   4   5
 *      6   7   8    9
 *   10   11  12   13   14
 * </pre>
 */
public class Board {

    private static final int NUM_HOLES = 15;
    private static final int[][] JUMPS = new int[][]{
            new int[]{0, 1, 3},
            new int[]{0, 2, 5},

            new int[]{1, 3, 6},
            new int[]{1, 4, 8},

            new int[]{2, 4, 7},
            new int[]{2, 5, 9},

            new int[]{3, 1, 0},
            new int[]{3, 4, 5},
            new int[]{3, 6, 10},
            new int[]{3, 7, 12},

            new int[]{4, 7, 11},
            new int[]{4, 8, 13},

            new int[]{5, 2, 0},
            new int[]{5, 4, 3},
            new int[]{5, 8, 12},
            new int[]{5, 9, 14},

            new int[]{6, 3, 1},
            new int[]{6, 7, 8},

            new int[]{7, 4, 2},
            new int[]{7, 8, 9},

            new int[]{8, 7, 6},
            new int[]{8, 4, 1},

            new int[]{9, 5, 2},
            new int[]{9, 8, 7},

            new int[]{10, 6, 3},
            new int[]{10, 11, 12},

            new int[]{11, 7, 4},
            new int[]{11, 12, 13},

            new int[]{12, 7, 3},
            new int[]{12, 8, 5},
            new int[]{12, 11, 10},
            new int[]{12, 13, 14},

            new int[]{13, 8, 4},
            new int[]{13, 12, 11},

            new int[]{14, 9, 5},
            new int[]{14, 13, 12}
    };

    private boolean[] holes = new boolean[NUM_HOLES];
    private final int startHole;
    private Collection<Jump> jumps = new LinkedList<>();

    /**
     * Creates a new Board
     *
     * @param startHole the empty hole to place when starting the game
     */
    public Board(int startHole) {
        ensure(startHole >= 0 && startHole < NUM_HOLES, "Invalid hole given: " + startHole + ", expected a number between 0 and " + NUM_HOLES);

        this.startHole = startHole;
        for (int i = 0; i < NUM_HOLES; ++i) {
            holes[i] = (i != startHole);
        }
    }

    /**
     * Gets all possible jumps for the current board.
     *
     * @return all available jumps
     */
    public Collection<Jump> getAvailableJumps() {
        List<Jump> jumps = new LinkedList<>();

        for (int i = 0; i < JUMPS.length; ++i) {
            addIfValid(jumps, JUMPS[i]);
        }

        return jumps;
    }

    /**
     * Applies the given jump to this board.
     *
     * @param jump
     */
    public void apply(Jump jump) {
        ensure(isValidJump(jump.from, jump.over, jump.to), "Received an invalid jump: " + jump);

        holes[jump.from] = false;
        holes[jump.over] = false;
        holes[jump.to] = true;

        jumps.add(jump);
    }

    /**
     * Gets the number of pegs still on the board.
     *
     * @return number of pegs left
     */
    public int getNumPegs() {
        int numPegs = 0;

        for (int i = 0; i < NUM_HOLES; ++i) {
            if (holes[i]) {
                numPegs += 1;
            }
        }

        return numPegs;
    }

    /**
     * Gets the hole that was empty at the beginning of the game.
     *
     * @return start hole
     */
    public int getStartHole() {
        return startHole;
    }

    /**
     * Clones this board
     *
     * @return clone
     */
    public Board clone() {
        Board clone = new Board(0);
        System.arraycopy(holes, 0, clone.holes, 0, NUM_HOLES);
        clone.jumps.addAll(jumps);
        return clone;
    }

    /**
     * Gets all the jumps applid to this board.
     *
     * @return jumps
     */
    public Collection<Jump> getJumps() {
        return jumps;
    }

    private void addIfValid(List<Jump> jumps, int[] jump) {
        int from = jump[0];
        int over = jump[1];
        int to = jump[2];

        if (isValidJump(from, over, to)) {
            jumps.add(new Jump(from, over, to));
        }
    }

    private boolean isValidJump(int from, int over, int to) {
        return holes[from] && holes[over] && !holes[to];
    }

    /**
     * Prints the current board
     */
    public void print() {
        System.out.println(String.format("           %c             ", ch(0)));
        System.out.println(String.format("         %c   %c          ", ch(1), ch(2)));
        System.out.println(String.format("       %c   %c   %c       ", ch(3), ch(4), ch(5)));
        System.out.println(String.format("     %c   %c   %c   %c    ", ch(6), ch(7), ch(8), ch(9)));
        System.out.println(String.format("   %c   %c   %c   %c   %c ", ch(10), ch(11), ch(12), ch(13), ch(14)));
    }

    private char ch(int i) {
        return holes[i] ? 'x' : 'o';
    }

    /**
     * A jump on the board.
     */
    public static class Jump {
        private int from;
        private int over;
        private int to;

        private Jump(int from, int over, int to) {
            this.from = from;
            this.over = over;
            this.to = to;
        }

        @Override
        public String toString() {
            return String.format("(from=%d,over=%d,to=%d)", from, over, to);
        }
    }
}