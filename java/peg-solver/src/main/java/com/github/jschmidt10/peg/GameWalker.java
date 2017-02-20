package com.github.jschmidt10.peg;

import java.util.Scanner;

/**
 * Walks through a board and all of the jumps applied to it so the user can see the progression.
 */
public class GameWalker {

    /**
     * Walks through the given board.
     *
     * @param board
     */
    public static void walk(Board board) {
        System.out.println("Found a winning board. Printing the progression.");
        try (Scanner scanner = new Scanner(System.in)) {
            Board runningBoard = new Board(board.getStartHole());

            System.out.println("Press 'Enter' to progress to the next turn:");
            runningBoard.print();
            scanner.nextLine();

            board.getJumps().forEach(jump -> {
                runningBoard.apply(jump);

                System.out.println("Press 'Enter' to progress to the next turn:");
                runningBoard.print();
                scanner.nextLine();
            });
        }
    }
}
