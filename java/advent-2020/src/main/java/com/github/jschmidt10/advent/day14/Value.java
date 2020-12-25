package com.github.jschmidt10.advent.day14;

public class Value {
  public final int NUM_BITS = 36;

  public final long longValue;
  public final String binaryString;

  Value(char[] binaryString) {
    this(new String(binaryString));
  }

  Value(String binaryString) {
    this(Long.parseLong(binaryString, 2), binaryString);
  }

  Value(long longValue) {
    this(longValue, Long.toBinaryString(longValue));
  }

  private Value(long longValue, String binaryString) {
    this.longValue = longValue;
    this.binaryString = leftPadWithZeroes(binaryString, NUM_BITS);
  }

  public static String leftPadWithZeroes(String str, int numBits) {
    int numZeroes = Math.max(0, (numBits - str.length()));

    StringBuilder sb = new StringBuilder();
    sb.append("0".repeat(numZeroes));
    sb.append(str);
    return sb.toString();
  }

  @Override
  public String toString() {
    return "Value{" +
        "longValue=" + longValue +
        ", binaryString='" + binaryString + '\'' +
        '}';
  }
}
