package com.github.jschmidt10.advent.day04.passport;

import com.github.jschmidt10.advent.day04.validator.RegexValidator;

public class PassportIdValidator extends RegexValidator {

  private static final String PASSPORT_ID_REGEX = "[0-9]{9}";

  public PassportIdValidator() {
    super(PASSPORT_ID_REGEX);
  }
}
