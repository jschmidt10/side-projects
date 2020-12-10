package com.github.jschmidt10.advent.day09;

import com.github.jschmidt10.advent.util.StringInputFile;
import java.util.List;

public class Day09 {
  public static void main(String[] args) {
    int preambleSize = 25;

    String filename = "input/day09/input.txt";
    StringInputFile inputFile = new StringInputFile(filename);
    long[] numbers = readLongArray(inputFile);

    long partOneAnswer = runPartOne(numbers, preambleSize);
    System.out.println("Part One: " + partOneAnswer);

    long partTwoAnswer = runPartTwo(numbers, partOneAnswer);
    System.out.println("Part Two: " + partTwoAnswer);
  }

  private static long runPartTwo(long[] numbers, long partOneAnswer) {
    long sum = 0;

    int low = 0;
    int high = 0;

    while (sum != partOneAnswer) {
      while (sum < partOneAnswer) {
        sum += numbers[high++];
      }
      while (sum > partOneAnswer) {
        sum -= numbers[low++];
      }
    }

    long min = Long.MAX_VALUE;
    long max = Long.MIN_VALUE;

    for (int i = low; i < high; i++) {
      min = Math.min(min, numbers[i]);
      max = Math.max(max, numbers[i]);
    }

    return min + max;
  }

  private static long runPartOne(long[] numbers, int preambleSize) {
    SumTable sumTable = new SumTable();
    for (int i = 0; i < preambleSize; i++) {
      sumTable.add(numbers[i]);
    }

    for (int i = preambleSize; i < numbers.length; i++) {
      if (!sumTable.isPossibleSum(numbers[i])) {
        return numbers[i];
      }
      else {
        sumTable.remove(numbers[i - preambleSize]);
        sumTable.add(numbers[i]);
      }
    }
    throw new IllegalStateException("No solution");
  }

  private static long[] readLongArray(StringInputFile inputFile) {
    List<String> lines = inputFile.getLines();

    long[] array = new long[lines.size()];

    int i = 0;
    for (String numberStr : lines) {
      array[i++] = Long.parseLong(numberStr);
    }

    return array;
  }
}
