package com.github.jschmidt10.advent.day04.passport;

import com.github.jschmidt10.advent.day04.validator.AlwaysValid;
import com.github.jschmidt10.advent.day04.validator.FieldValidator;
import java.util.Arrays;

public enum  PassportField {
  BIRTH_YEAR("byr", true, new BirthyearValidator()),
  ISSUE_YEAR("iyr", true, new IssueYearValidator()),
  EXPIRE_YEAR("eyr", true, new ExpireYearValidator()),
  HEIGHT("hgt", true, new HeightValidator()),
  HAIR_COLOR("hcl", true, new HairColorValidator()),
  EYE_COLOR("ecl", true, new EyeColorValidator()),
  PASSPORT_ID("pid", true, new PassportIdValidator()),
  COUNTRY_ID("cid", false, new AlwaysValid());

  public final String tag;
  public final boolean isRequired;
  public final FieldValidator fieldValidator;

  PassportField(String tag, boolean isRequired,
      FieldValidator fieldValidator) {
    this.tag = tag;
    this.isRequired = isRequired;
    this.fieldValidator = fieldValidator;
  }

  public static PassportField getField(String tag) {
    return Arrays
        .stream(values())
        .filter(field -> field.tag.equals(tag))
        .findAny()
        .orElseThrow(() -> new IllegalArgumentException("Unknown tag: " + tag));
  }
}
