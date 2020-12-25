package com.github.jschmidt10.advent.day14;

public class SetMaskInstruction {
  public static final String MASK_PREFIX = "mask = ";

  public final Bitmask bitmask;

  private SetMaskInstruction(String bitmask) {
    this.bitmask = new Bitmask(bitmask);
  }

  public static SetMaskInstruction fromLine(String line) {
    if (!line.startsWith(MASK_PREFIX)) {
      return null;
    }

    String newMask = line.substring(MASK_PREFIX.length());
    return new SetMaskInstruction(newMask);
  }

  @Override
  public String toString() {
    return "SetMaskInstruction{" +
        "newMask='" + bitmask + '\'' +
        '}';
  }
}
