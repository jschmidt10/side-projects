package com.github.jschmidt10.advent.day04;

import java.util.Map;
import java.util.TreeMap;

class Guard {
    private static final int MIN_PER_HOUR = 60;

    int id;
    int totalMinSlept = 0;
    Map<String, boolean[]> sleepRecords = new TreeMap<>();
    int sleepiestMin = -1;
    int[] sleepByMinCount = new int[MIN_PER_HOUR];

    void sleep(String date, int start, int end) {
        boolean[] sleepRecord = sleepRecords.computeIfAbsent(date, k -> new boolean[MIN_PER_HOUR]);
        for (int i = start; i < end; i++) {
            sleepRecord[i] = true;
            sleepByMinCount[i]++;
            if (sleepiestMin == -1 || sleepByMinCount[i] > sleepByMinCount[sleepiestMin]) {
                sleepiestMin = i;
            }
        }
        totalMinSlept += (end - start);
    }

    @Override
    public String toString() {
        return String.format("Elf(%d,%d,%s)", id, totalMinSlept, sleepRecords);
    }
}
