package com.github.jschmidt10.advent.day12;

public class Point {

  public final int x;
  public final int y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Point rotateLeft() {
    return new Point(-y, x);
  }

  public Point rotateRight() {
    return new Point(y, -x);
  }

  public Point translate(Vector vector) {
    return translate(vector.x, vector.y);
  }

  public Point translate(int dx, int dy) {
    return new Point(x + dx, y + dy);
  }

  public Vector subtract(Point other) {
    return new Vector(x - other.x, y - other.y);
  }

  @Override
  public String toString() {
    return "Coordinate{" +
        "x=" + x +
        ", y=" + y +
        '}';
  }
}
