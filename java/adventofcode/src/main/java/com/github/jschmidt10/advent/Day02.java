package com.github.jschmidt10.advent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Day02 {

    // Map that can be reused for all box ids
    private static Map<Character, Integer> charCounts = new TreeMap<>();

    public static void main(String[] args) throws FileNotFoundException {
        String filename = "src/main/resources/day02_inputs.txt";
        solvePart1(filename);
        solvePart2(filename);
    }

    private static void solvePart2(String filename) throws FileNotFoundException {
        List<String> inputs = new ArrayList<>();

        // Read all input
        try (Scanner scanner = new Scanner(new FileInputStream(filename))) {
            while (scanner.hasNextLine()) {
                inputs.add(scanner.nextLine());
            }
        }

        for (int i = 0; i < inputs.size(); i++) {
            String id1 = inputs.get(i);

            for (int j = i + 1; j < inputs.size(); j++) {
                String id2 = inputs.get(j);
                int diffIndex = getSingleDiffIndex(id1, id2);
                if (diffIndex >= 0) {
                    System.out.println(id1.substring(0, diffIndex) + id1.substring(diffIndex + 1, id1.length()));
                }
            }
        }
    }

    private static int getSingleDiffIndex(String id1, String id2) {
        int diffCharCount = 0;
        int diffIndex = -1;

        for (int i = 0; i < id1.length(); i++) {
            if (id1.charAt(i) != id2.charAt(i)) {
                diffCharCount++;
                diffIndex = i;
            }
            if (diffCharCount > 1) {
                return -1;
            }
        }

        return diffIndex;
    }

    private static void solvePart1(String filename) throws FileNotFoundException {
        State state = new State();

        try (Scanner scanner = new Scanner(new FileInputStream(filename))) {
            while (scanner.hasNextLine()) {
                processLine(scanner.nextLine(), state);
            }
        }

        System.out.println("doubles = " + state.doubles + ", triples = " + state.triples);
        System.out.println(state.getChecksum());
    }

    private static void processLine(String line, State state) {
        charCounts.clear();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            charCounts.merge(c, 1, Integer::sum);
        }

        boolean foundDouble = false;
        boolean foundTriple = false;

        for (int count : charCounts.values()) {
            if (!foundDouble && count == 2) {
                state.doubles += 1;
                foundDouble = true;
            } else if (!foundTriple && count == 3) {
                state.triples += 1;
                foundTriple = true;
            } else if (foundDouble && foundTriple) {
                break;
            }
        }
    }

    private static class State {
        int doubles = 0;
        int triples = 0;

        int getChecksum() {
            return doubles * triples;
        }
    }
}
