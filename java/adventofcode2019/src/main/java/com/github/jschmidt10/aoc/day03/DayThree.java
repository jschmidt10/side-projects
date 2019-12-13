package com.github.jschmidt10.aoc.day03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DayThree {
    public static void main(String[] args) throws IOException {
        String filename = "data/day3.txt";

        List<String> wireLines;
        try (Stream<String> lines = Files.lines(Paths.get(filename))) {
            wireLines = lines.collect(Collectors.toList());
        }

        Breadboard board = new Breadboard();

        List<WireStep> wireOneSteps = parseWireSteps(wireLines.get(0));
        Wire wire1 = new Wire(1, new Point(0, 0));

        for (WireStep step : wireOneSteps) {
            for (Point p : step.apply(wire1)) {
                wire1.append(p);
                board.place(wire1, p);
            }
        }

        int minDist = Integer.MAX_VALUE;

        List<WireStep> wireTwoSteps = parseWireSteps(wireLines.get(1));
        Wire wire2 = new Wire(2, new Point(0, 0));

        for (WireStep step : wireTwoSteps) {
            for (Point p : step.apply(wire2)) {
                wire2.append(p);
                if (board.place(wire2, p)) {
                    int dist = p.distFromOrigin();
                    if (dist < minDist && dist != 0) {
                        minDist = dist;
                    }
                }
            }
        }

        System.out.println("part1: " + minDist);
        System.out.println("part2: " + board.getMinIntersection());
    }

    private static List<WireStep> parseWireSteps(String line) {
        return Arrays.stream(line.split(",")).map(WireStep::fromStr).collect(Collectors.toList());
    }
}
