package com.github.jschmidt10.advent.day05;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day05 {

    private static Polymer polymer;

    public static void main(String[] args) throws IOException {
        String filename = "src/main/resources/day05_inputs.txt";
//        String filename = "src/main/resources/day05_test.txt";
//        solvePart1(filename);

        long start = System.currentTimeMillis();
        solvePart2(filename);
        long end = System.currentTimeMillis();

        System.out.println("Runtime: " + (end - start) + " ms");
    }

    private static void solvePart2(String filename) throws IOException {
        String line = Files.lines(Paths.get(filename)).findFirst().get();
        Polymer polymer = new Polymer(line);

        System.out.println("Original : " + polymer);

        int shortestChain = Integer.MAX_VALUE;
        for (char c = 'a'; c <= 'z'; c++) {
            Polymer newPolymer = polymer.removeUnit(c);
            newPolymer.react();
            if (newPolymer.chain.size() < shortestChain) {
                shortestChain = newPolymer.chain.size();
            }
        }

        System.out.println("Shortest polymer " + shortestChain);
    }

    private static void solvePart1(String filename) throws IOException {
        String line = Files.lines(Paths.get(filename)).findFirst().get();
        Polymer polymer = new Polymer(line);
        polymer.react();
        System.out.println("Done reacting: " + polymer.chain.size());
    }

    static class Polymer {

        List<Character> chain;

        Polymer(String chain) {
            this.chain = new ArrayList<>(chain.length());
            for (char c : chain.toCharArray()) {
                this.chain.add(c);
            }
        }

        Polymer(List<Character> chain) {
            this.chain = chain;
        }

        Polymer removeUnit(char remove) {
            char removeUpper = Character.toUpperCase(remove);
            List<Character> leftover = new ArrayList<>(chain.size());

            for (int i = 0; i < chain.size(); i++) {
                char c = chain.get(i);
                if (Character.toUpperCase(c) != removeUpper) {
                    leftover.add(c);
                }
            }

            return new Polymer(leftover);
        }

        void react() {
            for (int i = 0; i < chain.size() - 1; i++) {
                char left = chain.get(i);
                char right = chain.get(i + 1);

                if (isReactive(left, right)) {
                    chain.remove(i);
                    chain.remove(i);
                    i -= 2;
                    if (i < -1) {
                        i = -1;
                    }
                }
            }
        }

        private boolean isReactive(char l, char r) {
            return l != r && Character.toUpperCase(l) == Character.toUpperCase(r);
        }

        @Override
        public String toString() {
            return "Polymer(" + chain.stream().map(c -> c.toString()).collect(Collectors.joining()) + ")";
        }
    }
}
