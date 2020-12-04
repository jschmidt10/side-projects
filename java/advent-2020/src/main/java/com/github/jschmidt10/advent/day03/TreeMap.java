package com.github.jschmidt10.advent.day03;

import java.util.List;

public class TreeMap {

  private static final char NO_TREE = '.';
  private static final char TREE = '#';

  public final boolean[][] hasTree;
  public final int width;
  public final int height;

  private TreeMap(boolean[][] hasTree) {
    this.hasTree = hasTree;
    this.width = hasTree.length;
    this.height = hasTree[0].length;
  }

  public static TreeMap fromLines(List<String> lines) {
    int height = lines.size();
    int width = lines.get(0).length();

    boolean[][] hasTree = new boolean[width][height];

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        hasTree[x][y] = lines.get(y).charAt(x) == TREE;
      }
    }

    return new TreeMap(hasTree);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("TreeMap\n");

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (hasTree[x][y]) {
          sb.append(TREE);
        }
        else {
          sb.append(NO_TREE);
        }
        sb.append(' ');
      }
      sb.append('\n');
    }

    return sb.toString();
  }
}
