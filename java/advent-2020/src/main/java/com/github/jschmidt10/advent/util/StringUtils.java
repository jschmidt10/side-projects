package com.github.jschmidt10.advent.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class StringUtils {
  private StringUtils() {
    // don't instantiate - utility
  }

  public static Stream<Character> streamChars(String str) {
    return str
        .chars()
        .mapToObj(c -> (char) c);
  }

  public static List<Character> asCharList(String str) {
    return streamChars(str)
        .collect(Collectors.toList());
  }
}
