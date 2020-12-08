package com.github.jschmidt10.advent.day07;

import com.github.jschmidt10.advent.util.StringInputFile;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Day07 {

  public static final String SHINY_GOLD = "shiny gold";

  public static void main(String[] args) {
    String filename = "input/day07/input.txt";
    StringInputFile inputFile = new StringInputFile(filename);

    BagRegistry bagRegistry = new BagRegistry();

    for (String line : inputFile.getLines()) {
      bagRegistry.registerBag(line);
    }

    Bag shinyGoldBag = bagRegistry.getBag(SHINY_GOLD);

    System.out.println("Part One: " + countParents(shinyGoldBag));
    // getTotalBags counts the bag itself, we want to exclude the shiny gold bag so we subtract
    System.out.println("Part Two: " + (shinyGoldBag.getTotalBags() - 1));
  }

  public static int countParents(Bag shinyGoldBag) {
    Set<Bag> visited = new HashSet<>();
    Queue<Bag> toVisit = new LinkedList<>();
    toVisit.add(shinyGoldBag);

    while (!toVisit.isEmpty()) {
      Bag nextBag = toVisit.remove();
      visited.add(nextBag);

      Set<Bag> parents = nextBag.getParents();
      for (Bag parent : parents) {
        if (!visited.contains(parent)) {
          toVisit.add(parent);
        }
        toVisit.addAll(parents);
      }
    }

    // The shiny gold bag itself was visited
    return visited.size() - 1;
  }
}
