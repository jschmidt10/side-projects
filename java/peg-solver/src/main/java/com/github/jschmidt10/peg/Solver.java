package com.github.jschmidt10.peg;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

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

        initialBoard
                .getAvailableJumps()
                .stream()
                .map(j -> new Tuple2<>(initialBoard.clone(), j))
                .forEach(queue::add);

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
                jumps
                        .stream()
                        .map(j -> new Tuple2<>(board.clone(), j))
                        .forEach(queue::add);
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