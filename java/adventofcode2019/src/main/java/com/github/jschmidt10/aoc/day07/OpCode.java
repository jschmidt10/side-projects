package com.github.jschmidt10.aoc.day07;

import java.util.Arrays;

public class OpCode {

    public final Instruction instruction;
    public final int[] paramModes;

    public OpCode(Instruction instruction, int[] paramModes) {
        this.instruction = instruction;
        this.paramModes = paramModes;
    }

    public static OpCode fromStr(String opCode) {
        Instruction instruction = Instruction.getByOpNum(getLastTwo(opCode));

        String paramModeStr = reverse(padZeroes(getAllButLastTwo(opCode), instruction.getNumParams()));
        int[] paramModes = getArrayFromStr(paramModeStr);

        return new OpCode(instruction, paramModes);
    }

    private static String padZeroes(String s, int length) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < (length - s.length()); i++) {
            sb.append('0');
        }
        sb.append(s);

        return sb.toString();
    }

    private static int[] getArrayFromStr(String s) {
        int[] paramModes = new int[s.length()];

        for (int i = 0; i < paramModes.length; i++) {
            paramModes[i] = Integer.valueOf(s.substring(i, i+1));
        }

        return paramModes;
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

    public void execute(Program program) {
        instruction.execute(program, this);
    }

    @Override
    public String toString() {
        return "OpCode{" +
                "instruction=" + instruction +
                ", paramModes=" + Arrays.toString(paramModes) +
                '}';
    }
}
