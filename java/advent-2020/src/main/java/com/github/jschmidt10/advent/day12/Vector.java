package com.github.jschmidt10.advent.day12;

public class Vector {
  public final int x;
  public final int y;

  public Vector(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Vector scale(int factor) {
    return new Vector(x * factor, y * factor);
  }
}
