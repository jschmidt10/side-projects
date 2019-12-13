package com.github.jschmidt10.aoc.day05;

import java.util.Arrays;

public enum ParamMode {
    POSITION(0), IMMEDIATE(1);

    private final int value;

    ParamMode(int value) {
        this.value = value;
    }

    public static ParamMode[] getArrayFromStr(int numParams, String s) {
        ParamMode[] paramModes = new ParamMode[numParams];

        for (int i = 0; i < paramModes.length; i++) {
            if (i < s.length()) {
                paramModes[i] = ParamMode.getByValue(charAt(s, i));
            }
            else {
                paramModes[i] = ParamMode.POSITION;
            }
        }

        return paramModes;
    }

    private static String charAt(String s, int index) {
        return String.valueOf(s.charAt(index));
    }

    public static ParamMode getByValue(String value) {
        return getByValue(Integer.parseInt(value));
    }

    public static ParamMode getByValue(int value) {
        return Arrays.stream(values()).filter(mode -> mode.value == value).findAny().orElseThrow(() -> new IllegalArgumentException("Unknown ParamMode " + value));
    }

    public int fetch(int[] program, int addr) {
        int param = program[addr];

        switch (this) {
            case POSITION:
                return program[param];
            case IMMEDIATE:
                return param;
            default:
                throw new UnsupportedOperationException("fetch not implemented for " + this);
        }
    }
}
