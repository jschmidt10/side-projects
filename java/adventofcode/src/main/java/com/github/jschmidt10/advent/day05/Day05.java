package com.github.jschmidt10.advent.day05;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day05 {

    private static Polymer polymer;

    public static void main(String[] args) throws IOException {
        String filename = "src/main/resources/day05_inputs.txt";
//        solvePart1(filename);
        solvePart2(filename);
    }

    private static void solvePart2(String filename) throws IOException {
        String line = Files.lines(Paths.get(filename)).findFirst().get();
        Polymer polymer = new Polymer(line);

        System.out.println("Original : " + polymer);

        int shortestChain = Integer.MAX_VALUE;
        for (char c = 'a'; c <= 'z'; c++) {
            Polymer newPolymer = polymer.removeUnit(c);
            while (newPolymer.react()) ;
            if (newPolymer.chain.length() < shortestChain) {
                shortestChain = newPolymer.chain.length();
            }
        }

        System.out.println("Shortest polymer " + shortestChain);
    }

    private static void solvePart1(String filename) throws IOException {
        String line = Files.lines(Paths.get(filename)).findFirst().get();
        Polymer polymer = new Polymer(line);

        while (polymer.react()) ;

        System.out.println("Done reacting: " + polymer.chain.length());
    }

    static class Polymer {

        String chain;

        Polymer(String chain) {
            this.chain = chain;
        }

        Polymer removeUnit(char remove) {
            char removeUpper = Character.toUpperCase(remove);
            char[] leftover = new char[chain.length()];
            int next = 0;

            for (int i = 0; i < chain.length(); i++) {
                char c = chain.charAt(i);
                if (Character.toUpperCase(c) != removeUpper) {
                    leftover[next++] = c;
                }
            }

            return new Polymer(new String(leftover, 0, next));
        }

        boolean react() {
            for (int i = 0; i < chain.length() - 1; i++) {
                char first = chain.charAt(i);
                char second = chain.charAt(i + 1);

                char upperFirst = Character.toUpperCase(first);
                char upperSecond = Character.toUpperCase(second);

                if (first != second && upperFirst == upperSecond) {
                    this.chain = chain.substring(0, i) + chain.substring(i + 2, chain.length());
                    return true;
                }
            }

            return false;
        }

        @Override
        public String toString() {
            return "Polymer(" + chain + ")";
        }
    }
}
