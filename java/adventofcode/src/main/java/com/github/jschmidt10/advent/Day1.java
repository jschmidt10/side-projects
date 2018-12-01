package com.github.jschmidt10.advent;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Day1 {
    public static void main(String[] args) throws IOException {
        String inputFile = "src/main/resources/day1_inputs.txt";
        solvePart1(inputFile);
        solvePart2(inputFile);
    }

    private static void solvePart2(String inputFile) throws IOException {
        Set<Long> seenFrequencies = new TreeSet<>();
        long currentFrequency = 0;
        seenFrequencies.add(currentFrequency);

        while (true) {
            try (Scanner scanner = new Scanner(new FileInputStream(inputFile))) {
                while (scanner.hasNextLine()) {
                    currentFrequency += Long.parseLong(scanner.nextLine());
                    if (!seenFrequencies.add(currentFrequency)) {
                        System.out.println(currentFrequency);
                        return;
                    }
                }
            }
        }
    }

    private static void solvePart1(String inputFile) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputFile))) {
            System.out.println(reader.lines().map(Long::parseLong).mapToLong(Long::longValue).sum());
        }
    }
}
