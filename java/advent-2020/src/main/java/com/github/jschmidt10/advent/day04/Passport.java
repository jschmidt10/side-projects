package com.github.jschmidt10.advent.day04;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class Passport {

  private static final Pattern HAIR_COLOR_PATTERN = Pattern.compile("#[0-9a-f]{6}");
  private static final Pattern PASSPORT_ID_PATTERN = Pattern.compile("[0-9]{9}");

  private static final Set<String> REQUIRED_FIELDS = Set.of(
      "byr",
      "iyr",
      "eyr",
      "hgt",
      "hcl",
      "ecl",
      "pid"
  );

  private static final Set<String> VALID_EYE_COLORS = Set.of(
      "amb",
      "blu",
      "brn",
      "gry",
      "grn",
      "hzl",
      "oth"
  );

  private final Map<String,String> fields;

  public Passport(Map<String, String> fields) {
    this.fields = new TreeMap<>(fields);
  }

  public boolean hasRequiredFields() {
    return REQUIRED_FIELDS
        .stream()
        .allMatch(fields::containsKey);
  }

  public boolean isValid() {
    return hasRequiredFields() && fields
        .entrySet()
        .stream()
        .allMatch(entry -> isFieldValid(entry.getKey(), entry.getValue()));
  }

  private boolean isFieldValid(String field, String value) {
    switch (field) {
      case "byr":
        return isValidBirthYear(value);
      case "iyr":
        return isValidIssueYear(value);
      case "eyr":
        return isValidExpirationYear(value);
      case "hgt":
        return isValidHeight(value);
      case "hcl":
        return isValidHairColor(value);
      case "ecl":
        return isValidEyeColor(value);
      case "pid":
        return isValidPassportId(value);
      case "cid":
        return isValidCountryId(value);
      default:
        throw new IllegalArgumentException("Unknown field: " + field);
    }
  }

  private boolean isValidCountryId(String value) {
    return true;
  }

  private boolean isValidPassportId(String value) {
    return PASSPORT_ID_PATTERN.matcher(value).matches();
  }

  private boolean isValidEyeColor(String value) {
    return VALID_EYE_COLORS.contains(value);
  }

  private boolean isValidHairColor(String value) {
    return HAIR_COLOR_PATTERN.matcher(value).matches();
  }

  public boolean isValidHeight(String value) {
    String num = value.substring(0, value.length() - 2);
    String units = value.substring(value.length() - 2);

    switch (units) {
      case "cm":
        return isIntBetween(num, 150, 193);
      case "in":
        return isIntBetween(num, 59, 76);
      default:
        return false;
    }
  }

  private boolean isValidExpirationYear(String value) {
    return isIntBetween(value, 2020, 2030);
  }

  private boolean isValidIssueYear(String value) {
    return isIntBetween(value, 2010, 2020);
  }

  private boolean isValidBirthYear(String value) {
    return isIntBetween(value, 1920, 2002);
  }

  private boolean isIntBetween(String value, int minYear, int maxYear) {
    int n = Integer.parseInt(value);
    return n >= minYear && n <= maxYear;
  }

  @Override
  public String toString() {
    return "Passport{" +
        "fields=" + fields + ',' +
        "hasRequiredFields=" + hasRequiredFields() + ',' +
        "isValid=" + isValid() +
        '}';
  }

  public static class Builder {
    private Map<String, String> fields = new HashMap<>();

    public Builder addField(String field, String value) {
      fields.put(field, value);
      return this;
    }

    public void clear() {
      fields.clear();
    }

    public Passport build() {
      return new Passport(fields);
    }
  }
}
