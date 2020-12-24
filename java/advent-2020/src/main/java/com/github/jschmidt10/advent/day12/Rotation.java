package com.github.jschmidt10.advent.day12;

public enum Rotation {
  NORTH('N'),
  SOUTH('S'),
  EAST('E'),
  WEST('W');

  private final char c;

  Rotation(char c) {
    this.c = c;
  }

  public static Rotation fromChar(char c) {
    for (Rotation rotation : values()) {
      if (rotation.c == c) {
        return rotation;
      }
    }
    throw new IllegalArgumentException("Unknown Rotation character: " + c);
  }

  public Rotation rotateLeftOnce() {
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

  public Rotation rotateRightOnce() {
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
