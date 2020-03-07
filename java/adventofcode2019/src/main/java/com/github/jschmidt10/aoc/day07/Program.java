package com.github.jschmidt10.aoc.day07;

import java.util.concurrent.ArrayBlockingQueue;

public class Program {

    public static final int POSITION_PARAM = 0;
    public static final int IMMEDIATE_PARAM = 1;

    private final int[] binary;

    private ArrayBlockingQueue<Integer> input;
    private ArrayBlockingQueue<Integer> output;

    private int pc = 0;

    public Program(int[] binary) {
        this.binary = binary;
        this.input = new ArrayBlockingQueue<>(100);
        this.output = new ArrayBlockingQueue<>(100);
    }

    public void execute() {
        OpCode op = null;

        do {
            op = OpCode.fromStr(String.valueOf(binary[pc]));
            op.execute(this);
        } while (op.instruction != Instruction.HALT);
    }

    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    public ArrayBlockingQueue<Integer> getInput() {
        return input;
    }

    public ArrayBlockingQueue<Integer> getOutput() {
        return output;
    }

    public int fetchArgument(int mode, int offset) {
        switch (mode) {
            case POSITION_PARAM:
                return binary[binary[offset]];
            case IMMEDIATE_PARAM:
                return binary[offset];
            default:
                throw new IllegalArgumentException("Unknown ParamMode: " + mode);
        }
    }

    public int fetch(int offset) {
        return binary[offset];
    }

    public void store(int offset, int value) {
        binary[offset] = value;
    }

    public Program clone() {
        return new Program(binary);
    }
}
