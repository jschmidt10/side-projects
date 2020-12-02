package com.github.jschmidt10.advent.day02;

import com.github.jschmidt10.advent.util.StringInputFile;

public class Day02 {
  public static void main(String[] args) {
    String filename = "input/day02/input.txt";

    int validOccurences = 0;
    int validPositionals = 0;

    for (String line : new StringInputFile(filename).getLines()) {
      String[] tokens = line.split(": ", 2);

      String ruleDefinition = tokens[0];
      String password = tokens[1];

      PasswordRule passwordRule = PasswordRule.fromStr(ruleDefinition);

      if (passwordRule.acceptOccurrences(password)) {
        validOccurences++;
      }
      if (passwordRule.acceptsPositional(password)) {
        validPositionals++;
      }
    }

    System.out.println("Num Valid Occurrences: " + validOccurences);
    System.out.println("Num Valid Positionals: " + validPositionals);
  }
}
