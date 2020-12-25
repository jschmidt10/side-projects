package com.github.jschmidt10.advent.day14;

import com.github.jschmidt10.advent.util.StringInputFile;

public class Day14 {
  public static void main(String[] args) {
    String filename = "input/day14/input.txt";
    StringInputFile inputFile = new StringInputFile(filename);

    Bitmask bitmask = null;
    Memory partOneMemory = new Memory();
    Memory partTwoMemory = new Memory();

    for (String line : inputFile.getLines()) {
      SetMaskInstruction setMask = SetMaskInstruction.fromLine(line);
      if (setMask != null) {
        bitmask = setMask.bitmask;
      }
      else {
        SetMemInstruction setMem = SetMemInstruction.fromLine(line);
        long address = setMem.address;

        // part one
        Value maskedValue = bitmask.maskValue(setMem.value);
        partOneMemory.load(address, maskedValue);

        // part two
        Value addressValue = new Value(address);
        for (Value maskedAddress : bitmask.maskAddress(addressValue)) {
          partTwoMemory.load(maskedAddress.longValue, setMem.value);
        }
      }
    }

    System.out.println("Part One: " + partOneMemory.sum());
    System.out.println("Part Two: " + partTwoMemory.sum());
  }
}
