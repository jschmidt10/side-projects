package com.github.jschmidt10.advent.day14;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bitmask {
  private static final char X = 'X';

  private char[] mask;

  Bitmask(String bitmask) {
    mask = bitmask.toCharArray();
  }

  public Value maskValue(Value value) {
    char[] maskedBinaryString = value.binaryString.toCharArray();

    for (int i = 0; i < maskedBinaryString.length; i++) {
      if (mask[i] != X) {
        maskedBinaryString[i] = mask[i];
      }
    }

    return new Value(maskedBinaryString);
  }

  public List<Value> maskAddress(Value value) {
    int numFloating = countFloating();
    int numResults = (int) Math.pow(2, numFloating);

    List<Value> values = new ArrayList<>(numResults);

    for (int i = 0; i < numResults; i++) {
      String replacements = Value.leftPadWithZeroes(Long.toBinaryString(i), numFloating);
      values.add(makeReplacements(value, replacements));
    }

    return values;
  }

  private Value makeReplacements(Value value, String replacement) {
    int replacementIndex = 0;
    char[] result = Arrays.copyOf(mask, mask.length);

    for (int i = 0; i < result.length; i++) {
      if (result[i] == X) {
        result[i] = replacement.charAt(replacementIndex);
        replacementIndex++;
      }
      else if (result[i] == '1') {
        result[i] = '1';
      } else {
        result[i] = value.binaryString.charAt(i);
      }
    }

    return new Value(result);
  }

  private int countFloating() {
    int numFloating = 0;

    for (int i = 0; i < mask.length; i++) {
      if (mask[i] == X) {
        numFloating++;
      }
    }

    return numFloating;
  }

  @Override
  public String toString() {
    return "Bitmask{" +
        "mask=" + new String(mask) +
        '}';
  }
}
