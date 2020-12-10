package com.github.jschmidt10.advent.day08;

import java.util.Arrays;

public class Program {

  private final Instruction[] instructions;

  private boolean[] executed;
  private int programCounter;
  private int accumulator;

  public Program(Instruction[] instructions) {
    this.instructions = instructions;
    this.executed = new boolean[instructions.length];
    this.programCounter = 0;
    this.accumulator = 0;
  }

  public void runWithSubstitution() {
    while (isNotTerminated()) {
      int previosPc = programCounter;

      Instruction instruction = instructions[programCounter];
      Instruction substitution = instruction.substitute();

      if (substitution != null) {
        Program substitute = createSubstitute(substitution);
        substitute.runUntilInfiniteLoop();

        if (substitute.isComplete()) {
          accumulator = substitute.accumulator;
          programCounter = substitute.programCounter;
          return;
        }
      }

      executeInstruction();
      executed[previosPc] = true;
    }
  }

  private Program createSubstitute(Instruction substitution) {
    Instruction[] instructionsCopy = Arrays.copyOf(instructions, instructions.length);
    instructionsCopy[programCounter] = substitution;

    Program program = new Program(instructionsCopy);
    program.executed = Arrays.copyOf(executed, executed.length);
    program.programCounter = programCounter;
    program.accumulator = accumulator;

    return program;
  }

  public void runUntilInfiniteLoop() {
    while (isNotTerminated()) {
      int previosPc = programCounter;
      executeInstruction();
      executed[previosPc] = true;
    }
  }

  private boolean isNotTerminated() {
    return !isComplete() && !executed[programCounter];
  }

  public int getAccumulator() {
    return accumulator;
  }

  public boolean isComplete() {
    return programCounter == instructions.length;
  }

  private void executeInstruction() {
    Instruction instruction = instructions[programCounter];
    execute(instruction);
  }

  private void execute(Instruction instruction) {
    switch (instruction.op) {
      case ACC:
        accumulator += instruction.arg;
        programCounter++;
        break;
      case JMP:
        programCounter += instruction.arg;
        break;
      case NOP:
        programCounter++;
        break;
    }
  }
}
