package com.github.jschmidt10.advent.day13;

import com.github.jschmidt10.advent.util.StringInputFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day13 {
  public static void main(String[] args) {
    String filename = "input/day13/input.txt";
    StringInputFile inputFile = new StringInputFile(filename);
    List<String> lines = inputFile.getLines();

    int arrivalTime = Integer.parseInt(lines.get(0));

    runPartOne(lines, arrivalTime);
    runPartTwo(lines);
  }

  private static void runPartTwo(List<String> lines) {
    String[] tokens = lines.get(1).split(",");
    List<BusInstance> busInstances = new ArrayList<>(tokens.length);

    for (int i = 0; i < tokens.length; i++) {
      if (!"x".equals(tokens[i])) {
        int busTime = Integer.parseInt(tokens[i]);
        busInstances.add(new BusInstance(busTime, i));
      }
    }

    long allProduct = 1;
    long ans = 0;
    for (int i = 0; i < busInstances.size(); i++) {
      BusInstance busInstance = busInstances.get(i);

      allProduct *= busInstance.busTime;
      int targetModulo = adjustModulus(-busInstance.timeOffset, busInstance.busTime);

      long currentTerm = 1;
      for (int j = 0; j < busInstances.size(); j++) {
        if (i != j) {
          currentTerm *= busInstances.get(j).busTime;
        }
      }

      long factor = 1;
      while ((currentTerm * factor) % busInstance.busTime != targetModulo) {
        factor++;
      }

      ans += (currentTerm * factor);
    }

    while (ans - allProduct > 0) {
      ans -= allProduct;
    }

    System.out.println("Part Two: " + ans);
  }

  private static int adjustModulus(int modulo, int base) {
    while (modulo < 0) {
      modulo += base;
    }
    while (modulo > base) {
      modulo -= base;
    }
    return modulo;
  }

  private static void runPartOne(List<String> lines, int arrivalTime) {
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

  private static class BusInstance {
    public final int busTime;
    public final int timeOffset;

    public BusInstance(int busTime, int timeOffset) {
      this.busTime = busTime;
      this.timeOffset = timeOffset;
    }

    @Override
    public String toString() {
      return "BusInstance{" +
          "busTime=" + busTime +
          ", timeOffset=" + timeOffset +
          '}';
    }
  }
}
