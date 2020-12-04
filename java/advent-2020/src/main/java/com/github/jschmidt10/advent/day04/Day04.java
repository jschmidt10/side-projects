package com.github.jschmidt10.advent.day04;

import com.github.jschmidt10.advent.day04.InputFieldParser.InputField;
import com.github.jschmidt10.advent.day04.passport.Passport;
import com.github.jschmidt10.advent.day04.passport.Passport.Builder;
import com.github.jschmidt10.advent.util.StringInputFile;
import java.util.Collection;
import java.util.List;

public class Day04 {

  private final InputFieldParser inputFieldParser;

  private int hasRequiredFields = 0;
  private int isValid = 0;

  public Day04() {
    inputFieldParser = new InputFieldParser();
  }

  public static void main(String[] args) {
    String filename = "input/day04/input.txt";
    StringInputFile inputFile = new StringInputFile(filename);
    new Day04().run(inputFile.getLines());
  }

  private void run(List<String> lines) {
    Passport.Builder passportBuilder = new Passport.Builder();

    for (String line : lines) {
      if (isEndOfPassportFields(line)) {
        checkValidity(passportBuilder.build());
        passportBuilder.clear();
      }
      else {
        Collection<InputField> inputFields = inputFieldParser.parseFields(line);
        addFields(passportBuilder, inputFields);
      }
    }

    checkValidity(passportBuilder.build());

    System.out.println("Part One: " + hasRequiredFields);
    System.out.println("Part Two: " + isValid);
  }

  private boolean isEndOfPassportFields(String line) {
    return line.isBlank();
  }

  private void checkValidity(Passport passport) {
    if (passport.hasRequiredFields()) {
      hasRequiredFields++;
    }
    if (passport.isValid()) {
      isValid++;
    }
  }

  private void addFields(Builder passportBuilder, Collection<InputField> inputFields) {
    for (InputField inputField : inputFields) {
      passportBuilder.addField(inputField.key, inputField.value);
    }
  }
}
