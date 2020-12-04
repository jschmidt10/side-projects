package com.github.jschmidt10.advent.day04.passport;

import com.github.jschmidt10.advent.day04.validator.RegexValidator;

public class HairColorValidator extends RegexValidator {

  private static final String HAIR_COLOR_REGEX = "#[0-9a-f]{6}";

  public HairColorValidator() {
    super(HAIR_COLOR_REGEX);
  }
}
