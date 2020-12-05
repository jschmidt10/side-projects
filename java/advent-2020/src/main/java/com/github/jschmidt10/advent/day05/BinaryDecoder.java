package com.github.jschmidt10.advent.day05;

public class BinaryDecoder {

  private final char zeroChar;
  private final char oneChar;

  public BinaryDecoder(char zeroChar, char oneChar) {
    this.zeroChar = zeroChar;
    this.oneChar = oneChar;
  }

  public int decode(String encodedStr) {
    String binaryStr = asBinaryStr(encodedStr);
    return Integer.parseInt(binaryStr, 2);
  }

  private String asBinaryStr(String encodedStr) {
    char[] binaryChars = new char[encodedStr.length()];

    for (int i = 0; i < encodedStr.length(); i++) {
      char c = encodedStr.charAt(i);

      if (c == zeroChar) {
        binaryChars[i] = '0';
      }
      else if (c == oneChar){
        binaryChars[i] = '1';
      }
      else {
        throw new IllegalArgumentException("Unexpected character: " + c);
      }
    }

    return new String(binaryChars);
  }
}
