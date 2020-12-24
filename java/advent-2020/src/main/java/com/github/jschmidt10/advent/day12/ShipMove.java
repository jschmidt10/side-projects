package com.github.jschmidt10.advent.day12;

public class ShipMove {
  public final Direction direction;
  public final int num;

  public ShipMove(Direction direction, int num) {
    this.direction = direction;
    this.num = num;
  }

  public static ShipMove fromStr(String s) {
    char c = s.charAt(0);
    int num = Integer.parseInt(s.substring(1));

    return new ShipMove(Direction.fromChar(c), num);
  }

  @Override
  public String toString() {
    return "ShipMove{" +
        "direction=" + direction +
        ", num=" + num +
        '}';
  }
}
