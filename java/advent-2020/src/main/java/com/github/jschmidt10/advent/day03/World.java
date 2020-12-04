package com.github.jschmidt10.advent.day03;

public class World {
  private int x;
  private int y;
  private final TreeMap treeMap;

  public World(TreeMap treeMap) {
    this.x = 0;
    this.y = 0;
    this.treeMap = treeMap;
  }

  public boolean move(int dx, int dy) {
    x = x + dx;
    y = y + dy;

    return y < treeMap.height;
  }

  public boolean currentPosHasTree() {
    return treeMap.hasTree[x % treeMap.width][y];
  }
}
