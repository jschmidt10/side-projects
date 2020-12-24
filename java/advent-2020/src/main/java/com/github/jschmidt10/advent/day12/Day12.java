package com.github.jschmidt10.advent.day12;

import com.github.jschmidt10.advent.util.StringInputFile;

public class Day12 {
  public static void main(String[] args) {
    String filename = "input/day12/input.txt";
    StringInputFile inputFile = new StringInputFile(filename);

    Ship ship = new Ship();
    WaypointShip waypointShip = new WaypointShip(new Point(10, 1));

    for (String line : inputFile.getLines()) {
      ShipMove shipMove = ShipMove.fromStr(line);
      ship.move(shipMove.direction, shipMove.num);
      waypointShip.move(shipMove.direction, shipMove.num);
    }

    System.out.println("Part One: " + ship.getDistanceMoved());
    System.out.println("Part Two: " + waypointShip.getDistanceMoved());
  }
}
