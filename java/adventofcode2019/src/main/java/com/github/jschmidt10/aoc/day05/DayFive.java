package com.github.jschmidt10.aoc.day05;

import com.github.jschmidt10.aoc.day02.DayTwo;

import java.io.IOException;
import java.util.Arrays;

public class DayFive {
    public static void main(String[] args) throws IOException {
        String filename = "data/day5.txt";
        int[] binary = unbox(DayTwo.parseProgram(filename));

        Program program = new Program(binary);
        program.execute();
    }

    private static int[] unbox(Integer[] array) {
        return Arrays.stream(array).mapToInt(x -> x).toArray();
    }
}
