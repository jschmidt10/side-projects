package com.github.jschmidt10.advent.day04.validator;

import java.util.regex.Pattern;

public class RegexValidator implements FieldValidator {

  private final Pattern pattern;

  public RegexValidator(String regex) {
    this.pattern = Pattern.compile(regex);
  }

  @Override
  public boolean isValid(String value) {
    return pattern.matcher(value).matches();
  }
}
