package com.github.jschmidt10.advent.day04.passport;

import com.github.jschmidt10.advent.day04.validator.IntRangeValidator;

public class IssueYearValidator extends IntRangeValidator {

  private static final int MIN_YEAR = 2010;
  private static final int MAX_YEAR = 2020;

  public IssueYearValidator() {
    super(MIN_YEAR, MAX_YEAR);
  }
}
