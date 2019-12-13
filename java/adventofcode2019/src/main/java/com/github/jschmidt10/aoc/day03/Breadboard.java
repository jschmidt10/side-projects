package com.github.jschmidt10.aoc.day03;

import java.util.*;

public class Breadboard {

    // Point --> (wireId --> minSteps)
    private Map<Point, Map<Integer,Integer>> board = new HashMap<>();

    public boolean place(Wire wire, Point point) {
        Map<Integer,Integer> wiresToMinDist = board.computeIfAbsent(point, _k -> new TreeMap<>());

        boolean intersection = doWiresIntersect(wiresToMinDist, wire.id);

        Integer minDist = wiresToMinDist.get(wire.id);
        if (minDist == null || wire.getNumSteps() < minDist) {
            wiresToMinDist.put(wire.id, wire.getNumSteps());
        }

        return intersection;
    }

    public int getMinIntersection() {
        int minSteps = Integer.MAX_VALUE;

        for (Point p : board.keySet()) {
            Collection<Integer> values = board.get(p).values();

            if (values.size() > 1) {
                int numSteps = values.stream().mapToInt(k -> k).sum();
                if (numSteps < minSteps) {
                    minSteps = numSteps;
                }
            }
        }

        return minSteps;
    }

    private boolean doWiresIntersect(Map<Integer,Integer> wiresToMinDist, int wireId) {
        Set<Integer> wiresPresent = wiresToMinDist.keySet();
        return wiresPresent.size() > 0 && !wiresPresent.contains(wireId);
    }
}
