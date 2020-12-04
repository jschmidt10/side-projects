package com.github.jschmidt10.advent.day04.passport;

import com.github.jschmidt10.advent.day04.validator.FieldValidator;
import com.github.jschmidt10.advent.day04.validator.IntRangeValidator;

public class HeightValidator implements FieldValidator {

  private IntRangeValidator cmValidator = new IntRangeValidator(150, 193);
  private IntRangeValidator inValidator = new IntRangeValidator(59, 76);

  @Override
  public boolean isValid(String value) {
    String num = value.substring(0, value.length() - 2);
    String units = value.substring(value.length() - 2);

    switch (units) {
      case "cm":
        return cmValidator.isValid(num);
      case "in":
        return inValidator.isValid(num);
      default:
        return false;
    }
  }
}
