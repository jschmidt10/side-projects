package com.github.jschmidt10.aoc.day04;

public class DayFour {
    public static void main(String[] args) {
        int min = 240298;
        int max = 784956;

        int numPasswords = 0;
        for (int i = min; i <= max; i++) {
            String s = String.valueOf(i);
            if (hasDouble(s) && isNonDecreasing(s)) {
                numPasswords++;
            }
        }

        System.out.println("part1: " + numPasswords);

        numPasswords = 0;
        for (int i = min; i <= max; i++) {
            String s = String.valueOf(i);
            if (hasExactDouble(s) && isNonDecreasing(s)) {
                System.out.println(s);
                numPasswords++;
            }
        }

        System.out.println("part2: " + numPasswords);
    }

    private static boolean isNonDecreasing(String s) {
        for (int i = 0; i < s.length() - 1; i++) {
            char c1 = s.charAt(i);
            char c2 = s.charAt(i + 1);

            if (c1 > c2) {
                return false;
            }
        }

        return true;
    }

    private static boolean hasExactDouble(String s) {
        for (int i = 0; i < s.length() - 1; i++) {
            if (nextCharSame(s, i)) {
                if (previousCharDifferent(s, i) && nextCharDifferent(s, i + 1)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean nextCharSame(String s, int index) {
        return index < s.length() - 1 && s.charAt(index) == s.charAt(index + 1);
    }

    private static boolean previousCharDifferent(String s, int index) {
        return index == 0 || s.charAt(index - 1) != s.charAt(index);
    }

    private static boolean nextCharDifferent(String s, int index) {
        return index == s.length() - 1 || s.charAt(index) != s.charAt(index + 1);
    }

    private static boolean hasDouble(String s) {
        for (int i = 0; i < s.length() - 1; i++) {
            if (s.charAt(i) == s.charAt(i + 1)) {
                return true;
            }
        }

        return false;
    }
}
