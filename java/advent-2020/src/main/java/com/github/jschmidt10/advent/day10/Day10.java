package com.github.jschmidt10.advent.day10;

import com.github.jschmidt10.advent.util.IntInputFile;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class Day10 {
  public static void main(String[] args) {
    int joltDiff = 3;

    String filename = "input/day10/input.txt";
    IntInputFile inputFile = new IntInputFile(filename);
    List<Integer> numbers = inputFile.getLines();

    SortedSet<Integer> sortedNumbers = new TreeSet<>(numbers);

    runPartOne(sortedNumbers);
    runPartTwo(sortedNumbers, joltDiff);
  }

  private static void runPartTwo(SortedSet<Integer> sortedNumbers, int joltDiff) {
    Map<Integer,Long> possibilities = new HashMap<>();

    // Only one arrangement for the outlet
    possibilities.put(0, 1L);

    // Add our built-in adapter
    sortedNumbers.add(sortedNumbers.last() + joltDiff);

    for (Integer number : sortedNumbers) {
      long numPossibilities = 0;

      for (int i = 1; i <= joltDiff; i++) {
        int input = number - i;
        numPossibilities += possibilities.getOrDefault(input, 0L);
      }

      possibilities.put(number, numPossibilities);
    }

    long ans = possibilities.get(sortedNumbers.last());

    System.out.println("Part Two: " + ans);
  }

  private static void runPartOne(SortedSet<Integer> sortedNumbers) {
    int lastNum = 0;

    Map<Integer,Integer> deltaCounts = new TreeMap<>();

    for (Integer number : sortedNumbers) {
      int delta = number - lastNum;
      int newCount = deltaCounts.getOrDefault(delta, 0) + 1;

      deltaCounts.put(delta, newCount);
      lastNum = number;
    }

    deltaCounts.put(3, deltaCounts.get(3) + 1);

    int answer = deltaCounts.get(1) * deltaCounts.get(3);

    System.out.println("Part One: " + answer);
  }
}
