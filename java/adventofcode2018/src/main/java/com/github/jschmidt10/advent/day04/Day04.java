package com.github.jschmidt10.advent.day04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Day04 {
    private static Collection<Guard> guards;

    public static void main(String[] args) throws IOException {
        String filename = "src/main/resources/day04_inputs.txt";
        solvePart1(filename);
        solvePart2();
    }

    private static void solvePart2() {
        int maxSleepCount = 0;
        Guard sleepiest = null;

        for (Guard guard : guards) {
            if (guard.sleepiestMin >= 0) {
                int guardSleepCount = guard.sleepByMinCount[guard.sleepiestMin];
                if (maxSleepCount < guardSleepCount) {
                    maxSleepCount = guardSleepCount;
                    sleepiest = guard;
                }
            }
        }

        System.out.println("Part 2");
        System.out.println("Sleepiest guard is: " + sleepiest.id);
        System.out.println("His sleepiest minute is: " + sleepiest.sleepiestMin);
        System.out.println("Answer is " + (sleepiest.id * sleepiest.sleepiestMin));
    }

    private static void solvePart1(String filename) throws IOException {
        TreeSet<String> sortedLines = new TreeSet<>(Files.lines(Paths.get(filename)).collect(Collectors.toList()));
        GuardParser parser = new GuardParser(sortedLines);
        guards = parser.parse();

        Guard sleepiest = findSleepiest(guards);

        System.out.println("Part 1");
        System.out.println("Sleepiest guard is: " + sleepiest.id);
        System.out.println("His sleepiest minute is: " + sleepiest.sleepiestMin);
        System.out.println("Answer is " + (sleepiest.id * sleepiest.sleepiestMin));
    }

    private static Guard findSleepiest(Collection<Guard> guards) {
        Guard sleepiest = null;

        for (Guard guard : guards) {
            if (sleepiest == null) {
                sleepiest = guard;
            } else if (sleepiest.totalMinSlept < guard.totalMinSlept) {
                sleepiest = guard;
            }
        }

        return sleepiest;
    }
}
