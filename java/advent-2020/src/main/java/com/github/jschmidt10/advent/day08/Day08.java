package com.github.jschmidt10.advent.day08;

import com.github.jschmidt10.advent.util.StringInputFile;
import java.util.List;

public class Day08 {
  public static void main(String[] args) {
    String filename = "input/day08/input.txt";
    StringInputFile inputFile = new StringInputFile(filename);
    List<String> lines = inputFile.getLines();

    Instruction[] instructions = new Instruction[lines.size()];
    for (int i = 0; i < lines.size(); i++) {
      instructions[i] = Instruction.fromStr(lines.get(i));
    }

    Program program = new Program(instructions);
    program.runUntilInfiniteLoop();
    System.out.println("Part One: " + program.getAccumulator());

    program = new Program(instructions);
    program.runWithSubstitution();
    System.out.println("Part Two: " + program.getAccumulator());
  }
}
