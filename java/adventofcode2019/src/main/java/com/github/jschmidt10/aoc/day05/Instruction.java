package com.github.jschmidt10.aoc.day05;

import java.util.Arrays;
import java.util.Scanner;

public enum Instruction {
    ADD(1, 3) {
        @Override
        public Integer execute(int[] program, int pc, OpCode opCode) {
            int a = opCode.paramModes[0].fetch(program, pc + 1);
            int b = opCode.paramModes[1].fetch(program, pc + 2);

            int res = a + b;

            program[program[pc + 3]] = res;

            return null;
        }
    }, MULTIPLY(2, 3) {
        @Override
        public Integer execute(int[] program, int pc, OpCode opCode) {
            int a = opCode.paramModes[0].fetch(program, pc + 1);
            int b = opCode.paramModes[1].fetch(program, pc + 2);

            int res = a * b;

            program[program[pc + 3]] = res;

            return null;
        }
    }, READ(3, 1) {
        @Override
        public Integer execute(int[] program, int pc, OpCode opCode) {
            try (Scanner scanner = new Scanner(System.in)) {
                int value = scanner.nextInt();
                program[program[pc + 1]] = value;
            }
            return null;
        }
    }, PRINT(4, 1) {
        @Override
        public Integer execute(int[] program, int pc, OpCode opCode) {
            int res = opCode.paramModes[0].fetch(program, pc + 1);
            System.out.println(res);
            return null;
        }
    }, JUMP_IF_TRUE(5, 2) {
        @Override
        public Integer execute(int[] program, int pc, OpCode opCode) {
            int v = opCode.paramModes[0].fetch(program, pc + 1);
            if (v != 0) {
                return opCode.paramModes[1].fetch(program, pc + 2);
            } else {
                return null;
            }
        }
    }, JUMP_IF_FALSE(6, 2) {
        @Override
        public Integer execute(int[] program, int pc, OpCode opCode) {
            int v = opCode.paramModes[0].fetch(program, pc + 1);
            if (v == 0) {
                return opCode.paramModes[1].fetch(program, pc + 2);
            } else {
                return null;
            }
        }
    }, LESS_THAN(7, 3) {
        @Override
        public Integer execute(int[] program, int pc, OpCode opCode) {
            int a = opCode.paramModes[0].fetch(program, pc + 1);
            int b = opCode.paramModes[1].fetch(program, pc + 2);

            int res = a < b ? 1 : 0;

            program[program[pc + 3]] = res;

            return null;
        }
    }, EQUALS(8, 3) {
        @Override
        public Integer execute(int[] program, int pc, OpCode opCode) {
            int a = opCode.paramModes[0].fetch(program, pc + 1);
            int b = opCode.paramModes[1].fetch(program, pc + 2);

            int res = a == b ? 1 : 0;

            program[program[pc + 3]] = res;

            return null;
        }
    },
    HALT(99, 0) {
        @Override
        public Integer execute(int[] program, int pc, OpCode opCode) {
            return null;
        }
    };

    private final int opNum;
    private final int numParams;

    Instruction(int opNum, int numParams) {
        this.opNum = opNum;
        this.numParams = numParams;
    }

    public static Instruction getByOpNum(String opNum) {
        return getByOpNum(Integer.parseInt(opNum));
    }

    public static Instruction getByOpNum(int opNum) {
        return Arrays.stream(values()).filter(i -> i.opNum == opNum).findAny().orElseThrow(() -> new IllegalArgumentException("Unknown opNum: " + opNum));
    }

    public abstract Integer execute(int[] program, int pc, OpCode opCode);

    public int getNumParams() {
        return numParams;
    }
}
