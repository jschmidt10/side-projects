package com.github.jschmidt10.advent.day04.validator;

public class IntRangeValidator implements FieldValidator {

  private final int min;
  private final int max;

  public IntRangeValidator(int min, int max) {
    this.min = min;
    this.max = max;
  }

  @Override
  public boolean isValid(String value) {
    int num = Integer.parseInt(value);
    return min <= num && num <= max;
  }
}
