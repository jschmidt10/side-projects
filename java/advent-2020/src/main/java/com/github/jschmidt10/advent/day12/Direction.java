package com.github.jschmidt10.advent.day12;

public enum Direction {
  NORTH('N'),
  SOUTH('S'),
  EAST('E'),
  WEST('W'),
  LEFT('L'),
  RIGHT('R'),
  FORWARD('F');

  private final char c;

  Direction(char c) {
    this.c = c;
  }

  public static Direction fromChar(char c) {
    for (Direction direction : values()) {
      if (direction.c == c) {
        return direction;
      }
    }
    throw new IllegalArgumentException("No direction for char " + c);
  }

  public Direction rotateLeftOnce() {
    switch (this) {
      case NORTH:
        return WEST;
      case WEST:
        return SOUTH;
      case SOUTH:
        return EAST;
      case EAST:
        return NORTH;
      default:
        throw new IllegalArgumentException("Can only rotate cardinal directions");
    }
  }

  public Direction rotateRightOnce() {
    switch (this) {
      case NORTH:
        return EAST;
      case EAST:
        return SOUTH;
      case SOUTH:
        return WEST;
      case WEST:
        return NORTH;
      default:
        throw new IllegalArgumentException("Can only rotate cardinal directions");
    }
  }
}
