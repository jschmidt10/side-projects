package com.github.jschmidt10.advent.day13;

import com.github.jschmidt10.advent.util.StringInputFile;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day13 {
  public static void main(String[] args) {
    String filename = "input/day13/input.txt";
    StringInputFile inputFile = new StringInputFile(filename);
    List<String> lines = inputFile.getLines();

    int arrivalTime = Integer.parseInt(lines.get(0));
    List<Integer> busTimes = readBusTimes(lines.get(1));

    int fastestBus = 0;
    int minWait = Integer.MAX_VALUE;

    for (int busTime : busTimes) {
      int wait = busTime - (arrivalTime % busTime);

      if (wait < minWait) {
        minWait = wait;
        fastestBus = busTime;
      }
    }

    System.out.println("Part one: " + (minWait * fastestBus));
  }

  private static List<Integer> readBusTimes(String line) {
    return Arrays
        .stream(line.split(","))
        .filter(token -> !"x".equals(token))
        .map(Integer::parseInt)
        .collect(Collectors.toList());
  }
}
