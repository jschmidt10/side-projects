package com.github.jschmidt10.peg;

import java.util.*;

/**
 * Driver for the brute force solver.
 */
public class Solver {

    /**
     * Runs the brute force solver.
     *
     * @return completed games
     */
    public static Collection<Board> run(int startHole) {
        Queue<Tuple2<Board, Board.Jump>> queue = new LinkedList<>();
        Collection<Board> completed = new LinkedList<>();
        Board initialBoard = new Board(startHole);

        long start = System.currentTimeMillis();

        initialBoard.getAvailableJumps()
                .forEach(jump -> queue.add(new Tuple2<>(initialBoard.clone(), jump)));

        int numIterations = 0;

        while (!queue.isEmpty()) {
            numIterations += 1;

            Tuple2<Board, Board.Jump> tuple = queue.poll();
            Board board = tuple.getFirst();
            Board.Jump jump = tuple.getSecond();

            board.apply(jump);

            Collection<Board.Jump> jumps = board.getAvailableJumps();

            if (jumps.isEmpty()) {
                completed.add(board);
            } else {
                jumps.forEach(j -> queue.add(new Tuple2<>(board.clone(), j)));
            }
        }

        long end = System.currentTimeMillis();

        System.out.println("Ran " + numIterations + " iterations in " + (end - start) + " millis");

        return completed;
    }

    /**
     * Entry point for the game solver.
     *
     * @param args
     */
    public static void main(String[] args) {
        int startHole = 0;
        Collection<Board> completed = Solver.run(startHole);

        completed
                .stream()
                .filter(b -> b.getNumPegs() == 1)
                .findFirst()
                .ifPresent(GameWalker::walk);

        GameStats.print(completed);
    }
}