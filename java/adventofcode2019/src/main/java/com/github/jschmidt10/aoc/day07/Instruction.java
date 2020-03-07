package com.github.jschmidt10.aoc.day07;

import java.util.Arrays;

public enum Instruction {
    ADD(1, 3) {
        @Override
        public void execute(Program program, OpCode opCode) {
            int pc = program.getPc();

            int a = program.fetchArgument(opCode.paramModes[0], pc + 1);
            int b = program.fetchArgument(opCode.paramModes[1], pc + 2);

            int res = a + b;
            int resAddr = program.fetch(pc + 3);

            program.store(resAddr, res);
            program.setPc(pc + getNumParams() + 1);
        }
    }, MULTIPLY(2, 3) {
        @Override
        public void execute(Program program, OpCode opCode) {
            int pc = program.getPc();

            int a = program.fetchArgument(opCode.paramModes[0], pc + 1);
            int b = program.fetchArgument(opCode.paramModes[1], pc + 2);

            int res = a * b;
            int resAddr = program.fetch(pc + 3);

            program.store(resAddr, res);
            program.setPc(pc + getNumParams() + 1);
        }
    }, READ(3, 1) {
        @Override
        public void execute(Program program, OpCode opCode) {
            int pc = program.getPc();
            int resAddr = program.fetch(pc + 1);

            try {
                int res = program.getInput().take();
                program.store(resAddr, res);
            } catch (InterruptedException e) {
                throw new RuntimeException("Failed to read from input", e);
            }

            program.setPc(pc + getNumParams() + 1);
        }
    }, PRINT(4, 1) {
        @Override
        public void execute(Program program, OpCode opCode) {
            int pc = program.getPc();
            int res = program.fetchArgument(opCode.paramModes[0], pc + 1);

            program.getOutput().offer(res);
            program.setPc(pc + getNumParams() + 1);
        }
    }, JUMP_IF_TRUE(5, 2) {
        @Override
        public void execute(Program program, OpCode opCode) {
            int pc = program.getPc();
            int cond = program.fetchArgument(opCode.paramModes[0], pc + 1);

            if (cond != 0) {
                int nextAddr = program.fetchArgument(opCode.paramModes[1], pc + 2);
                program.setPc(nextAddr);
            } else {
                program.setPc(pc + getNumParams() + 1);
            }
        }
    }, JUMP_IF_FALSE(6, 2) {
        @Override
        public void execute(Program program, OpCode opCode) {
            int pc = program.getPc();
            int cond = program.fetchArgument(opCode.paramModes[0], pc + 1);

            if (cond == 0) {
                int nextAddr = program.fetchArgument(opCode.paramModes[1], pc + 2);
                program.setPc(nextAddr);
            } else {
                program.setPc(pc + getNumParams() + 1);
            }
        }
    }, LESS_THAN(7, 3) {
        @Override
        public void execute(Program program, OpCode opCode) {
            int pc = program.getPc();
            int a = program.fetchArgument(opCode.paramModes[0], pc + 1);
            int b = program.fetchArgument(opCode.paramModes[1], pc + 2);

            int res = a < b ? 1 : 0;

            program.store(program.fetch(pc + 3), res);
        }
    }, EQUALS(8, 3) {
        @Override
        public void execute(Program program, OpCode opCode) {
            int pc = program.getPc();
            int a = program.fetchArgument(opCode.paramModes[0], pc + 1);
            int b = program.fetchArgument(opCode.paramModes[1], pc + 2);

            int res = a == b ? 1 : 0;

            program.store(program.fetch(pc + 3), res);
        }
    },
    HALT(99, 0) {
        @Override
        public void execute(Program program, OpCode opCode) {
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

    public abstract void execute(Program program, OpCode opCode);

    public int getNumParams() {
        return numParams;
    }
}
