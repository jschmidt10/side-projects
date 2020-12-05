package com.github.jschmidt10.advent.day05;

public class AirlineSeatDecoder {
  private static final char FRONT_HALF = 'F';
  private static final char BACK_HALF = 'B';
  private static final char LEFT_HALF = 'L';
  private static final char RIGHT_HALF = 'R';

  private static final int ROW_STR_LENGTH = 7;

  private final BinaryDecoder rowDecoder;
  private final BinaryDecoder columnDecoder;

  public AirlineSeatDecoder() {
    rowDecoder = new BinaryDecoder(FRONT_HALF, BACK_HALF);
    columnDecoder = new BinaryDecoder(LEFT_HALF, RIGHT_HALF);
  }

  public AirlineSeat decode(String seatStr) {
    String rowStr = seatStr.substring(0, ROW_STR_LENGTH);
    String colStr = seatStr.substring(ROW_STR_LENGTH);

    int row = rowDecoder.decode(rowStr);
    int col = columnDecoder.decode(colStr);

    return new AirlineSeat(row, col);
  }
}
