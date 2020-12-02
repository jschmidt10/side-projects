package com.github.jschmidt10.advent.day02;

public class PasswordRule {

  final char character;
  final int min;
  final int max;

  public PasswordRule(char character, int min, int max) {
    this.character = character;
    this.min = min;
    this.max = max;
  }

  /**
   * Creates a password rule from the given String.
   *
   * Expected format:
   * MIN-MAX CHAR
   *
   * Example:
   * 1-3 a     // Must contain 1 to 3 a's
   *
   * @param str The definition String
   * @return PasswordRule
   */
  public static PasswordRule fromStr(String str) {
    String[] definitionTokens = str.split(" ", 2);
    String[] boundsTokens = definitionTokens[0].split("-", 2);

    int min = Integer.parseInt(boundsTokens[0]);
    int max = Integer.parseInt(boundsTokens[1]);
    char character = definitionTokens[1].charAt(0);

    return new PasswordRule(character, min, max);
  }

  public boolean acceptOccurrences(String password) {
    int occurrences = 0;

    for (char passwordChar : password.toCharArray()) {
      if (character == passwordChar) {
        occurrences++;

        if (occurrences > max) {
          return false;
        }
      }
    }

    return occurrences >= min;
  }

  public boolean acceptsPositional(String password) {
    boolean minMatches = password.charAt(min - 1) == character;
    boolean maxMatches = password.charAt(max - 1) == character;

    return minMatches ^ maxMatches;
  }
}
