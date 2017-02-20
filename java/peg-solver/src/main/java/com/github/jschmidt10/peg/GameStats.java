package com.github.jschmidt10.peg;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Records some high level counts about the game resolutions.
 */
public class GameStats {

    /**
     * Computes some game stats and prints them to stdout.
     *
     * @param completed
     */
    public static void print(Collection<Board> completed) {
        Map<Integer, Long> pegCounts = sumPegCounts(completed);
        long totalBoards = pegCounts.values().stream().reduce(0L, (x, y) -> x + y, (x, y) -> x + y);

        System.out.println("# Pegs Left     # Boards        % Boards");
        pegCounts.forEach((numPegs, numBoards) -> {
            double percentage = ((double) numBoards / totalBoards);
            System.out.println(String.format("%d        %d      %f", numPegs, numBoards, percentage));
        });
    }

    private static Map<Integer, Long> sumPegCounts(Collection<Board> completed) {
        Map<Integer, Long> counts = new HashMap<>();

        completed.forEach(board -> {
            int numPegs = board.getNumPegs();
            counts.putIfAbsent(numPegs, 0L);
            counts.compute(numPegs, (k, v) -> v + 1);
        });

        return counts;
    }
}
