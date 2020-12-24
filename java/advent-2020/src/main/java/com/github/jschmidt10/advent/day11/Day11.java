package com.github.jschmidt10.advent.day11;

import com.github.jschmidt10.advent.day11.WaitingArea.Seat;
import com.github.jschmidt10.advent.util.StringInputFile;
import java.util.List;

public class Day11 {
  public static void main(String[] args) {
    String filename = "input/day11/input.txt";
    StringInputFile inputFile = new StringInputFile(filename);

    WaitingArea waitingArea = parseWaitingArea(inputFile.getLines());

    while (waitingArea.wasMutated) {
      waitingArea = waitingArea.shuffleSeats();
    }

    int numSeats = waitingArea.getNumOccupied();
    System.out.println("Answer: " + numSeats);
  }

  private static WaitingArea parseWaitingArea(List<String> lines) {
    int height = lines.size();
    int width = lines.get(0).length();

    Seat[][] seats = new Seat[height][width];

    for (int i = 0; i < height; i++) {
      char[] line = lines.get(i).toCharArray();
      for (int j = 0; j < line.length; j++) {
        seats[i][j] = Seat.fromChar(line[j]);
      }
    }

    return new WaitingArea(seats);
  }
}
