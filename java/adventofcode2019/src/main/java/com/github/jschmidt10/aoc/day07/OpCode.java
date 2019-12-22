package com.github.jschmidt10.aoc.day05;

import java.util.Arrays;

public class OpCode {

    public final Instruction instruction;
    public final ParamMode[] paramModes;

    public OpCode(Instruction instruction, ParamMode[] paramModes) {
        this.instruction = instruction;
        this.paramModes = paramModes;
    }

    public static OpCode fromStr(String opCode) {
        Instruction instruction = Instruction.getByOpNum(getLastTwo(opCode));

        String paramModeStr = reverse(getAllButLastTwo(opCode));
        ParamMode[] paramModes = ParamMode.getArrayFromStr(instruction.getNumParams(), paramModeStr);

        return new OpCode(instruction, paramModes);
    }

    private static String reverse(String s) {
        return new StringBuilder(s).reverse().toString();
    }

    private static String getAllButLastTwo(String s) {
        return s.length() <= 2 ? "" : s.substring(0, s.length() - 2);
    }

    private static String getLastTwo(String s) {
        return s.length() <= 2 ? s : s.substring(s.length() - 2);
    }

    public Integer execute(int[] program, int pc) {
        return instruction.execute(program, pc, this);
    }

    @Override
    public String toString() {
        return "OpCode{" +
                "instruction=" + instruction +
                ", paramModes=" + Arrays.toString(paramModes) +
                '}';
    }
}
