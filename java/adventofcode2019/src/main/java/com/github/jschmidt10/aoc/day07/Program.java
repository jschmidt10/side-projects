package com.github.jschmidt10.aoc.day05;

public class Program {

    private final int[] binary;
    private int pc = 0;

    public Program(int[] binary) {
        this.binary = binary;
    }

    public void execute() {
        OpCode lastOp = null;

        do {
            lastOp = OpCode.fromStr(String.valueOf(binary[pc]));
            Integer newPc = lastOp.execute(binary, pc);

            if (newPc != null) {
                pc = newPc;
            }
            else {
                pc += lastOp.instruction.getNumParams() + 1;
            }

        } while (lastOp.instruction != Instruction.HALT);
    }
}
