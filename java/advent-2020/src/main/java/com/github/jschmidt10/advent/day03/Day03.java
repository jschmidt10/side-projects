package com.github.jschmidt10.advent.day03;

import com.github.jschmidt10.advent.util.StringInputFile;

public class Day03 {
  public static void main(String[] args) {
    String filename = "input/day03/input.txt";
    StringInputFile inputFile = new StringInputFile(filename);
    TreeMap treeMap = TreeMap.fromLines(inputFile.getLines());

    int partOneNumTrees = getNumTrees(treeMap, new Slope(3, 1));
    System.out.println("Part One: " + partOneNumTrees);

    Slope[] slopes = new Slope[] {
      new Slope(1, 1),
      new Slope(3, 1),
      new Slope(5, 1),
      new Slope(7, 1),
      new Slope(1, 2)
    };

    long product = 1;
    for (Slope slope : slopes) {
      product *= getNumTrees(treeMap, slope);
    }

    System.out.println("Part Two: " + product);
  }

  private static int getNumTrees(TreeMap treeMap, Slope slope) {
    World world = new World(treeMap);

    int totalTrees = 0;
    while (world.move(slope.dx, slope.dy)) {
      if (world.currentPosHasTree()) {
        totalTrees++;
      }
    }

    return totalTrees;
  }

  public static class Slope {
    public final int dx;
    public final int dy;

    public Slope(int dx, int dy) {
      this.dx = dx;
      this.dy = dy;
    }
  }
}
