package com.github.jschmidt10.aoc.day01;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class DayOne {

    public static void main(String[] args) throws IOException {
        String filename = "data/day1.txt";

        try (Stream<String> lines = Files.lines(Paths.get(filename))) {
            int sum = lines.map(Integer::parseInt).mapToInt(DayOne::calculateFuel).sum();
            System.out.println("part1: " + sum);
        }

        try (Stream<String> lines = Files.lines(Paths.get(filename))) {
            int sum = lines.map(Integer::parseInt).mapToInt(DayOne::calculateTotalFuel).sum();
            System.out.println("part2: " + sum);
        }
    }

    public static int calculateFuel(int mass) {
        return mass / 3 - 2;
    }

    public static int calculateTotalFuel(int mass) {
        int totalMass = 0;

        int deltaMass = DayOne.calculateFuel(mass);
        while (deltaMass > 0) {
            totalMass += deltaMass;
            deltaMass = DayOne.calculateFuel(deltaMass);
        }

        return totalMass;
    }
}
