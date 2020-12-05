package com.github.jschmidt10.advent.day05;

import java.util.Comparator;

public class AirlineSeat implements Comparable<AirlineSeat> {
  private static final Comparator<AirlineSeat> COMPARATOR =
      Comparator
          .comparing(AirlineSeat::getRow)
          .thenComparing(AirlineSeat::getColumn);

  private static final int NUM_COLUMNS = 8;

  public final int id;
  public final int row;
  public final int column;

  public AirlineSeat(int id) {
    this(id / NUM_COLUMNS, id % NUM_COLUMNS);
  }

  public AirlineSeat(int row, int column) {
    this.row = row;
    this.column = column;
    this.id = row * NUM_COLUMNS + column;
  }

  public int getRow() {
    return row;
  }

  public int getColumn() {
    return column;
  }

  @Override
  public int compareTo(AirlineSeat o) {
    return COMPARATOR.compare(this, o);
  }

  @Override
  public String toString() {
    return "AirlineSeat{" +
        "row=" + row +
        ", column=" + column +
        ", id=" + id +
        '}';
  }
}
