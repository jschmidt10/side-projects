package com.github.jschmidt10.advent.day08;

public class Instruction {

  public final Op op;
  public final int arg;

  public Instruction(Op op, int arg) {
    this.op = op;
    this.arg = arg;
  }

  public Instruction substitute() {
    Op substitutionOp = op.substitute();
    if (substitutionOp == null) {
      return null;
    }
    else {
      return new Instruction(substitutionOp, arg);
    }
  }

  public static Instruction fromStr(String line) {
    String[] tokens = line.split(" ");
    Op op = Op.getByCode(tokens[0]);
    int arg = Integer.parseInt(tokens[1]);

    return new Instruction(op, arg);
  }

  @Override
  public String toString() {
    return "Instruction{" +
        "op=" + op +
        ", arg=" + arg +
        '}';
  }

  public enum Op {
    ACC("acc"),
    JMP("jmp"),
    NOP("nop");

    public final String code;

    Op(String code) {
      this.code = code;
    }

    public Op substitute() {
      switch (this) {
        case JMP:
          return NOP;
        case NOP:
          return JMP;
      }
      return null;
    }

    public static Op getByCode(String code) {
      for (Op op : values()) {
        if (op.code.equals(code)) {
          return op;
        }
      }
      throw new IllegalArgumentException("No such op: " + code);
    }
  }
}
