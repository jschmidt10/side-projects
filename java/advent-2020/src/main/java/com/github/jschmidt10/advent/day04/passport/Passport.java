package com.github.jschmidt10.advent.day04.passport;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Passport {

  private final Map<PassportField,String> fields;

  public Passport(Map<PassportField, String> fields) {
    this.fields = new TreeMap<>(fields);
  }

  public boolean hasRequiredFields() {
    return Arrays.stream(PassportField.values())
        .filter(field -> field.isRequired)
        .allMatch(fields::containsKey);
  }

  public boolean isValid() {
    if (!hasRequiredFields()) {
      return false;
    }

    for (Entry<PassportField, String> entry : fields.entrySet()) {
      PassportField passportField = entry.getKey();
      String value = entry.getValue();

      if (!passportField.fieldValidator.isValid(value)) {
        return false;
      }
    }

    return true;
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
    private Map<PassportField, String> fields = new TreeMap<>();

    public Builder addField(String field, String value) {
      fields.put(PassportField.getField(field), value);
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
