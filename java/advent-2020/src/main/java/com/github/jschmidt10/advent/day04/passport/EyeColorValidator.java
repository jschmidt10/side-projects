package com.github.jschmidt10.advent.day04.passport;

import com.github.jschmidt10.advent.day04.validator.SetValidator;
import java.util.Set;

public class EyeColorValidator extends SetValidator {

  private static final Set<String> EYE_COLORS = Set.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");

  public EyeColorValidator() {
    super(EYE_COLORS);
  }
}
