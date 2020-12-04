package com.github.jschmidt10.advent.day04.validator;

import java.util.Set;

public class SetValidator implements FieldValidator {

  private final Set<String> validValues;

  public SetValidator(Set<String> validValues) {
    this.validValues = validValues;
  }

  @Override
  public boolean isValid(String value) {
    return validValues.contains(value);
  }
}
