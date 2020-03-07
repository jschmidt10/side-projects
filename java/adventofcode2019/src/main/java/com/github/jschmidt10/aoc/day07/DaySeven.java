package com.github.jschmidt10.aoc.day07;

import com.github.jschmidt10.aoc.day02.DayTwo;
import com.github.jschmidt10.aoc.day05.DayFive;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class DaySeven {

    public static void main(String[] args) throws Exception {
        String filename = "data/day7_sample.txt";

        int[] binary = DayFive.unbox(DayTwo.parseProgram(filename));
        Program program = new Program(binary);

        ExecutorService executor = Executors.newSingleThreadExecutor();

//        // @formatter:off
//        List<Future<Integer>> futures = executor.invokeAll(
//                permutations(Arrays.asList('0', '1', '2', '3', '4'))
//                    .stream()
//                    .map(DaySeven::strToArray)
//                    .map(arr -> new AmplifierRunner(arr, program))
//                    .collect(Collectors.toList())
//        );
//        // @formatter:on
//
//        int maxResult = futures.stream().mapToInt(DaySeven::getScore).max().getAsInt();
//        System.out.println("Part1: " + maxResult);

        Collection<int[]> permutations = permutations(Arrays.asList('5', '6', '7', '8', '9')).stream().map(DaySeven::strToArray).collect(Collectors.toList());

        Collection<Integer> results = new ArrayList<>(permutations.size());
        for (int[] permutation : permutations) {
            Program copy = program.clone();
            Future<?> future = executor.submit(new FeedbackAmplifierRunner(permutation, copy));
            copy.execute();
            future.cancel(true);

            results.add(getResult(copy));
        }

        System.out.println(results);
        int maxResult = results.stream().mapToInt(x -> x).max().getAsInt();
        System.out.println("Part2: " + maxResult);

        executor.shutdownNow();
    }

    private static int getResult(Program program) {
        Integer res = program.getOutput().poll();
        if (res != null) {
            return res;
        }
        else {
            program.getInput().poll();
            return program.getInput().poll();
        }
    }

    private static int getScore(Future<Integer> future) {
        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to Future.get", e);
        }
    }

    private static int[] strToArray(String s) {
        int[] arr = new int[s.length()];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = Integer.valueOf(s.substring(i, i + 1));
        }

        return arr;
    }

    private static Collection<String> permutations(Collection<Character> chars) {
        if (chars.isEmpty()) {
            return Collections.emptyList();
        } else if (chars.size() == 1) {
            char c = chars.iterator().next();
            return Collections.singleton(String.valueOf(c));
        } else {
            List<String> allPerms = new ArrayList<>();
            for (Character prefix : chars) {
                List<Character> rest = new ArrayList<>(chars);
                rest.remove(prefix);

                for (String suffix : permutations(rest)) {
                    allPerms.add(prefix + suffix);
                }
            }
            return allPerms;
        }
    }

    private static class AmplifierRunner implements Callable<Integer> {

        private final int[] phaseSettings;
        private final Program program;

        public AmplifierRunner(int[] phaseSettings, Program program) {
            this.phaseSettings = phaseSettings;
            this.program = program;
        }

        @Override
        public Integer call() {
            try {
                Program copy = program.clone();
                ArrayBlockingQueue<Integer> input = copy.getInput();
                ArrayBlockingQueue<Integer> output = copy.getOutput();

                input.offer(phaseSettings[0]);
                input.offer(0);

                copy.execute();

                for (int i = 1; i < phaseSettings.length; i++) {
                    int res = output.take();

                    copy = program.clone();
                    input = copy.getInput();
                    output = copy.getOutput();

                    input.offer(phaseSettings[i]);
                    input.offer(res);

                    copy.execute();
                }
                return output.take();
            } catch (InterruptedException e) {
                throw new RuntimeException("Failed to read from output", e);
            }
        }
    }

    private static class FeedbackAmplifierRunner implements Runnable {
        private final int[] phaseSettings;
        private final ArrayBlockingQueue<Integer> input;
        private final ArrayBlockingQueue<Integer> output;

        public FeedbackAmplifierRunner(int[] phaseSettings, Program program) {
            this.phaseSettings = phaseSettings;
            this.input = program.getInput();
            this.output = program.getOutput();
        }

        @Override
        public void run() {
            try {
                input.offer(phaseSettings[0]);
                input.offer(0);

                int nextPhase = 1;

                while(!Thread.currentThread().isInterrupted()) {
                    Integer res = output.poll(10, TimeUnit.MILLISECONDS);
                    if (res != null) {
                        input.offer(phaseSettings[nextPhase]);
                        input.offer(res);
                        nextPhase = (nextPhase + 1) % phaseSettings.length;
                    }
                }

            } catch (InterruptedException e) {
                // nothing
            }
        }
    }
}
