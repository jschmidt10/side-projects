package com.github.jschmidt10.advent.day04.passport;

import com.github.jschmidt10.advent.day04.validator.IntRangeValidator;

public class ExpireYearValidator extends IntRangeValidator {

  private static final int MIN_YEAR = 2020;
  private static final int MAX_YEAR = 2030;

  public ExpireYearValidator() {
    super(MIN_YEAR, MAX_YEAR);
  }
}
