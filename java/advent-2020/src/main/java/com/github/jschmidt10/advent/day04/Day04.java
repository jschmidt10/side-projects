package com.github.jschmidt10.advent.day04;

import com.github.jschmidt10.advent.util.StringInputFile;

public class Day04 {
  public static void main(String[] args) {
    String filename = "input/day04/input.txt";
    StringInputFile inputFile = new StringInputFile(filename);

    int hasRequiredFields = 0;
    int isValid = 0;

    Passport.Builder passportBuilder = new Passport.Builder();
    for (String line : inputFile.getLines()) {
      if (line.isBlank()) {
        // We have parsed a complete passport
        Passport passport = passportBuilder.build();
        if (passport.hasRequiredFields()) {
          hasRequiredFields++;
        }
        if (passport.isValid()) {
          isValid++;
        }
        passportBuilder.clear();
      }
      else {
        String[] fieldTokens = line.split(" ");
        for (String fieldToken : fieldTokens) {
          String[] tokens = fieldToken.split(":");
          passportBuilder.addField(tokens[0], tokens[1]);
        }
      }
    }

    Passport passport = passportBuilder.build();
    if (passport.hasRequiredFields()) {
      hasRequiredFields++;
    }
    if (passport.isValid()) {
      isValid++;
    }

    System.out.println("Part One: " + hasRequiredFields);
    System.out.println("Part Two: " + isValid);
  }
}
