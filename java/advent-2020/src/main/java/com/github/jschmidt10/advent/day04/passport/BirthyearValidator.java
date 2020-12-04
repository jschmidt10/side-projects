package com.github.jschmidt10.advent.day04.passport;

import com.github.jschmidt10.advent.day04.validator.IntRangeValidator;

public class BirthyearValidator extends IntRangeValidator {

  private static final int MIN_YEAR = 1920;
  private static final int MAX_YEAR = 2002;

  public BirthyearValidator() {
    super(MIN_YEAR, MAX_YEAR);
  }
}
