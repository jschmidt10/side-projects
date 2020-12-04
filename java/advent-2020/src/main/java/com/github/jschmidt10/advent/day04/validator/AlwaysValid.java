package com.github.jschmidt10.advent.day04.validator;

public class AlwaysValid implements FieldValidator {
  @Override
  public boolean isValid(String value) {
    return true;
  }
}
