package com.github.jschmidt10.advent.day14;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetMemInstruction {
  public static final Pattern SET_MEMORY_REGEX = Pattern.compile("^mem\\[([0-9]+)\\] = ([0-9]+)");

  public final long address;
  public final Value value;

  private SetMemInstruction(long address, Value value) {
    this.address = address;
    this.value = value;
  }

  public static SetMemInstruction fromLine(String line) {
    Matcher matcher = SET_MEMORY_REGEX.matcher(line);
    if (!matcher.matches()) {
      return null;
    }

    long address = Long.parseLong(matcher.group(1));
    long value = Long.parseLong(matcher.group(2));

    Value memoryValue = new Value(value);

    return new SetMemInstruction(address, memoryValue);
  }

  @Override
  public String toString() {
    return "SetMemInstruction{" +
        "address=" + address +
        ", value=" + value +
        '}';
  }
}
