package com.github.jschmidt10.advent.day03;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03 {

    private static final Pattern pattern = Pattern.compile("#([0-9]+) @ ([0-9]+),([0-9]+): ([0-9]+)x([0-9]+)");

    private static List<Claim> claims = new ArrayList<>();
    private static int[][] numClaims;
    private static int maxDimObserved = 0;

    public static void main(String[] args) throws FileNotFoundException {
        String filename = "src/main/resources/day03_inputs.txt";
        solvePart1(filename);
        solvePart2();
    }

    private static void solvePart2() throws FileNotFoundException {
        claims.forEach(claim -> {
            if (!claim.hasOverlaps(numClaims)) {
                System.out.println("Claim " + claim + " has no overlaps");
            }
        });
    }

    private static void solvePart1(String filename) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new FileInputStream(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Matcher matcher = pattern.matcher(line);
                if (!matcher.matches() || matcher.groupCount() != 5) {
                    throw new IllegalArgumentException("Found unexpected line: " + line);
                }

                Claim claim = new Claim(matcher);
                claims.add(claim);

                int widthNeeded = claim.left + claim.width;
                int heightNeeded = claim.top + claim.height;

                if (maxDimObserved < widthNeeded) {
                    maxDimObserved = widthNeeded;
                } else if (maxDimObserved < heightNeeded) {
                    maxDimObserved = heightNeeded;
                }
            }
        }

        System.out.println("Found max dimension of " + maxDimObserved);

        numClaims = new int[maxDimObserved][maxDimObserved];
        claims.forEach(claim -> claim.apply(numClaims));
        System.out.println("Number of multi-claims: " + countMultiClaims(numClaims));
    }

    private static int countMultiClaims(int[][] numClaims) {
        int count = 0;

        for (int i = 0; i < numClaims.length; i++) {
            for (int j = 0; j < numClaims[i].length; j++) {
                if (numClaims[i][j] > 1) {
                    count++;
                }
            }
        }

        return count;
    }

    private static class Claim {
        int id;
        int left, top;
        int width, height;

        Claim(Matcher matcher) {
            this.id = groupAsInt(matcher, 1);
            this.left = groupAsInt(matcher, 2);
            this.top = groupAsInt(matcher, 3);
            this.width = groupAsInt(matcher, 4);
            this.height = groupAsInt(matcher, 5);
        }

        private int groupAsInt(Matcher m, int groupNum) {
            return Integer.valueOf(m.group(groupNum));
        }

        void apply(int[][] numClaims) {
            for (int i = left; i < left + width; i++) {
                for (int j = top; j < top + height; j++) {
                    numClaims[i][j]++;
                }
            }
        }

        @Override
        public String toString() {
            return String.format("#%d @ %d,%d: %dx%d", id, left, top, width, height);
        }

        boolean hasOverlaps(int[][] numClaims) {
            for (int i = left; i < left + width; i++) {
                for (int j = top; j < top + height; j++) {
                    if (numClaims[i][j] > 1) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
