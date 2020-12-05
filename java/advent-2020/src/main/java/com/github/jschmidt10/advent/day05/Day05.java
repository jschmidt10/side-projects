package com.github.jschmidt10.advent.day05;

import com.github.jschmidt10.advent.util.StringInputFile;
import java.util.SortedSet;
import java.util.TreeSet;

public class Day05 {

  public static void main(String[] args) {
    String filename = "input/day05/input.txt";
    StringInputFile inputFile = new StringInputFile(filename);
    AirlineSeatDecoder airlineSeatDecoder = new AirlineSeatDecoder();

    SortedSet<AirlineSeat> sortedSeats = new TreeSet<>();

    for (String line : inputFile.getLines()) {
      AirlineSeat seat = airlineSeatDecoder.decode(line);
      sortedSeats.add(seat);
    }

    System.out.println("Part One: " + sortedSeats.last());
    System.out.println("Part Two: " + findMissingSeat(sortedSeats));

  }

  private static AirlineSeat findMissingSeat(SortedSet<AirlineSeat> sortedSeats) {
    AirlineSeat lastSeat = null;
    for (AirlineSeat seat : sortedSeats) {
      if (lastSeat != null) {
        if (seat.id - lastSeat.id > 1) {
          return new AirlineSeat(lastSeat.id + 1);
        }
      }
      lastSeat = seat;
    }
    throw new IllegalStateException("Found no missing seat!");
  }
}
