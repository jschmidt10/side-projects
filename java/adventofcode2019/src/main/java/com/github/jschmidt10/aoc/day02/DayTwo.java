package com.github.jschmidt10.aoc.day02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DayTwo {

    public static void main(String[] args) throws IOException {
        String filename = "data/day2.txt";

        Integer[] initialProgram = parseProgram(filename);

        Integer[] program = seedProgram(initialProgram, 12, 2);
        System.out.println("part1: " + runProgram(program));

        for (int noun = 0; noun < 100; noun++) {
            for (int verb = 0; verb < 100; verb++) {
                int ans = runProgram(seedProgram(initialProgram, noun, verb));

                if (19690720 == ans) {
                    System.out.println("part2: " + (100 * noun + verb));
                    return;
                }
            }
        }

        System.err.println("Found no answer");
    }

    public static Integer[] parseProgram(String filename) throws IOException {
        Integer[] initialProgram;
        try (Stream<String> lines = Files.lines(Paths.get(filename))) {
            initialProgram = lines.flatMap(line -> Arrays.stream(line.split(","))).map(Integer::parseInt).toArray(Integer[]::new);
        }
        return initialProgram;
    }

    private static Integer[] seedProgram(Integer[] initialProgram, int noun, int verb) {
        Integer[] program = Arrays.copyOf(initialProgram, initialProgram.length);

        program[1] = noun;
        program[2] = verb;

        return program;
    }

    private static int runProgram(Integer[] program) {
        int pc = 0;

        while (true) {
//            System.out.println("pc = " + pc + ", op = " + program[pc]);
            switch(program[pc]) {
                case 1:
                    performOp(program, pc, (x, y) -> x + y);
                    break;
                case 2:
                    performOp(program, pc, (x, y) -> x * y);
                    break;
                case 99:
                    return program[0];
                default:
                    throw new IllegalStateException("Unknown op code: " + program[pc]);
            }

            pc += 4;
        }
    }

    private static void performOp(Integer[] program, int pc, BinaryOperator<Integer> op) {
        int x = program[program[pc + 1]];
        int y = program[program[pc + 2]];
        program[program[pc + 3]] = op.apply(x, y);
    }
}
