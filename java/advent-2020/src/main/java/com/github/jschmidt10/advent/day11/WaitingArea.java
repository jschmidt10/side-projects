package com.github.jschmidt10.advent.day11;

public class WaitingArea {

  public final boolean wasMutated;
  private final int height;
  private final int width;
  private final Seat[][] seats;

  public WaitingArea(Seat[][] seats) {
    this(seats, true);
  }

  public WaitingArea(Seat[][] seats, boolean wasMutated) {
    this.wasMutated = wasMutated;
    this.height = seats.length;
    this.width = seats[0].length;
    this.seats = seats;
  }

  public int getNumOccupied() {
    int numOccupied = 0;

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        if (seats[i][j].isOccupied()) {
          numOccupied++;
        }
      }
    }

    return numOccupied;
  }

  public WaitingArea shuffleSeats() {
    boolean wasMutated = false;
    Seat[][] newSeats = new Seat[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        newSeats[i][j] = shuffleSeat(i, j);
        wasMutated = (wasMutated || (newSeats[i][j] != seats[i][j]));
      }
    }

    return new WaitingArea(newSeats, wasMutated);
  }

  private Seat shuffleSeat(int i, int j) {
    switch (seats[i][j]) {
      case NONE:
        return Seat.NONE;
      case EMPTY:
        return shuffleEmpty(i, j);
      case OCCUPIED:
        return shuffleOccupied(i, j);
      default:
        throw new IllegalArgumentException("Invalid seat type: " + seats[i][j]);
    }
  }

  private Seat shuffleEmpty(int i, int j) {
    int numOccupied = getAdjacentOccupiedBySightline(i, j);
    if (numOccupied == 0) {
      return Seat.OCCUPIED;
    } else {
      return Seat.EMPTY;
    }
  }

  private Seat shuffleOccupied(int i, int j) {
    int numOccupied = getAdjacentOccupiedBySightline(i, j);
    if (numOccupied >= 5) {
      return Seat.EMPTY;
    } else {
      return Seat.OCCUPIED;
    }
  }

  private int getAdjacentOccupied(int i, int j) {
    int numOccupied = 0;

    for (int di = i - 1; di <= i + 1; di++) {
      for (int dj = j - 1; dj <= j + 1; dj++) {
        if (!(i == di && j == dj) &&
            isWithinBounds(di, dj) &&
            seats[di][dj].isOccupied()) {
          numOccupied++;
        }
      }
    }

    return numOccupied;
  }

  private int getAdjacentOccupiedBySightline(int i, int j) {
    int numOccupied = 0;

    for (Direction dir : Direction.values()) {
      numOccupied += getAdjacentNumOccupied(i, j, dir);
    }

    return numOccupied;
  }

  private int getAdjacentNumOccupied(int i, int j, Direction dir) {
    int nextX = i + dir.di;
    int nextY = j + dir.dj;

    while (isWithinBounds(nextX, nextY)) {
      Seat seat = seats[nextX][nextY];
      if (seat.isOccupied()) {
        return 1;
      }
      else if (seat.isEmpty()) {
        return 0;
      }

      nextX = nextX + dir.di;
      nextY = nextY + dir.dj;
    }

    return 0;
  }

  private boolean isWithinBounds(int di, int dj) {
    return di >= 0 && di < height && dj >= 0 && dj < width;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        sb.append(seats[i][j]);
      }
      sb.append('\n');
    }

    return sb.toString();
  }

  public enum Direction {
    TOP_LEFT(-1, -1),
    TOP(-1, 0),
    TOP_RIGHT(-1, 1),
    RIGHT(0, 1),
    BOTTOM_RIGHT(1, 1),
    BOTTOM(1, 0),
    BOTTOM_LEFT(1, -1),
    LEFT(0, -1);

    public final int di;
    public final int dj;

    Direction(int di, int dj) {
      this.di = di;
      this.dj = dj;
    }
  }

  public enum Seat {
    NONE('.'), EMPTY('L'), OCCUPIED('#');

    private final char c;

    Seat(char c) {
      this.c = c;
    }

    public static Seat fromChar(char c) {
      for (Seat seat : values()) {
        if (seat.c == c) {
          return seat;
        }
      }
      throw new IllegalArgumentException("No seat has character " + c);
    }

    public boolean isOccupied() {
      return this.equals(OCCUPIED);
    }

    public boolean isEmpty() {
      return this.equals(EMPTY);
    }

    @Override
    public String toString() {
      return String.valueOf(c);
    }
  }
}
