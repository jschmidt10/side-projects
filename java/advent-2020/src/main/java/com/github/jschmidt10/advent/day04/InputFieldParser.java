package com.github.jschmidt10.advent.day04;

import java.util.ArrayList;
import java.util.Collection;

public class InputFieldParser {

  public Collection<InputField> parseFields(String line) {
    Collection<InputField> inputFields = new ArrayList<>();

    String[] fieldTokens = line.split(" ");
    for (String fieldToken : fieldTokens) {
      String[] tokens = fieldToken.split(":");
      inputFields.add(new InputField(tokens[0], tokens[1]));
    }

    return inputFields;
  }

  public static class InputField {
    public final String key;
    public final String value;

    public InputField(String key, String value) {
      this.key = key;
      this.value = value;
    }
  }
}
