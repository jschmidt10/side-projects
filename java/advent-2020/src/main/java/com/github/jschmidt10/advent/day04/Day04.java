package com.github.jschmidt10.advent.day04;

import com.github.jschmidt10.advent.day04.passport.Passport;
import com.github.jschmidt10.advent.day04.passport.Passport.Builder;
import com.github.jschmidt10.advent.util.StringInputFile;

public class Day04 {

  private int hasRequiredFields = 0;
  private int isValid = 0;

  public static void main(String[] args) {
    String filename = "input/day04/input.txt";
    new Day04().run(filename);
  }

  private void run(String filename) {
    StringInputFile inputFile = new StringInputFile(filename);
    Passport.Builder passportBuilder = new Passport.Builder();

    for (String line : inputFile.getLines()) {
      if (line.isBlank()) {
        // We have parsed a complete passport
        checkValidity(passportBuilder.build());
        passportBuilder.clear();
      }
      else {
        addFields(passportBuilder, line);
      }
    }

    checkValidity(passportBuilder.build());

    System.out.println("Part One: " + hasRequiredFields);
    System.out.println("Part Two: " + isValid);
  }

  private void checkValidity(Passport passport) {
    if (passport.hasRequiredFields()) {
      hasRequiredFields++;
    }
    if (passport.isValid()) {
      isValid++;
    }
  }

  private void addFields(Builder passportBuilder, String line) {
    String[] fieldTokens = line.split(" ");
    for (String fieldToken : fieldTokens) {
      String[] tokens = fieldToken.split(":");
      passportBuilder.addField(tokens[0], tokens[1]);
    }
  }
}
