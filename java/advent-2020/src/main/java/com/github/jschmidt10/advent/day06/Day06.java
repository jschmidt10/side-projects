package com.github.jschmidt10.advent.day06;

import com.github.jschmidt10.advent.util.StringInputFile;
import java.util.List;

public class Day06 {
  public static void main(String[] args) {
    String filename = "input/day06/input.txt";
    StringInputFile inputFile = new StringInputFile(filename);

    int totalYes = 0;
    int totalAllYes = 0;

    for (List<String> answerLines : inputFile.getGroupedLines()) {
      ClaimsFormOne claimsFormOne = new ClaimsFormOne(answerLines);
      totalYes += claimsFormOne.getNumYesAnswers();

      ClaimsFormTwo claimsFormTwo = new ClaimsFormTwo(answerLines);
      totalAllYes += claimsFormTwo.getNumYesAnswers();
    }

    System.out.println("Part One: " + totalYes);
    System.out.println("Part Two: " + totalAllYes);
  }
}
