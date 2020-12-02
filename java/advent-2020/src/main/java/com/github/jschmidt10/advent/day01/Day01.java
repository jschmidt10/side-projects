package com.github.jschmidt10.advent.day01;

import com.github.jschmidt10.advent.util.IntInputFile;
import java.util.SortedSet;

public class Day01 {
  public static void main(String[] args) {
    String inputFile = "input/day01/input.txt";
    int target = 2020;

    SortedSet<Integer> numbers = new IntInputFile(inputFile).sort();

    computePartOne(target, numbers);
    computePartTwo(target, numbers);
  }

  public static void computePartTwo(int target, SortedSet<Integer> numbers) {
    for (int x : numbers) {
      for (int y : numbers) {
        if (x != y) {
          int z = target - x - y;
          if (numbers.contains(z)) {
            System.out.println(x * y * z);
            return;
          }
        }
      }
    }
  }

  public static void computePartOne(int target, SortedSet<Integer> numbers) {
    for (Integer number : numbers) {
      int diff = target - number;
      if (numbers.contains(diff)) {
        System.out.println(number * diff);
        return;
      }
    }
  }
}
