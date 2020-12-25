package com.github.jschmidt10.advent.day14;

import java.util.HashMap;
import java.util.Map;

public class Memory {
  private Map<Long, Value> valueTable;

  Memory() {
    valueTable = new HashMap<>();
  }

  void load(long address, Value value) {
    valueTable.put(address, value);
  }

  long sum() {
    long sum = 0;

    for (Value value : valueTable.values()) {
      sum += value.longValue;
    }

    return sum;
  }

  @Override
  public String toString() {
    return "Memory{" +
        "valueTable=" + valueTable +
        '}';
  }
}
