package com.github.jschmidt10.advent.day04;

import java.util.Collection;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class GuardParser {

    private final Pattern guardLine = Pattern.compile("\\[[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}\\] Guard #([0-9]+) begins shift");
    private final Pattern startSleepLine = Pattern.compile("\\[[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:([0-9]{2})\\] falls asleep");
    private final Pattern endSleepLine = Pattern.compile("\\[([0-9]{4}-[0-9]{2}-[0-9]{2}) [0-9]{2}:([0-9]{2})\\] wakes up");

    private final SortedSet<String> input;
    private final Map<Integer, Guard> guards;

    private Guard currentGuard;
    private int currentStartSleep;

    GuardParser(SortedSet<String> input) {
        this.input = input;
        this.guards = new TreeMap<>();
    }

    Collection<Guard> parse() {
        for (String line : input) {
            if (!tryPattern(line, guardLine, this::processGuard)) {
                if (!tryPattern(line, startSleepLine, this::processStartSleep)) {
                    if (!tryPattern(line, endSleepLine, this::processEndSleep)) {
                        throw new IllegalArgumentException("Found an unexpected line: " + line);
                    }
                }
            }
        }

        return guards.values();
    }

    private void processGuard(Matcher m) {
        int currentGuardId = Integer.valueOf(m.group(1));

        currentGuard = guards.get(currentGuardId);
        if (currentGuard == null) {
            currentGuard = new Guard();
            currentGuard.id = currentGuardId;
            guards.put(currentGuardId, currentGuard);
        }
    }

    private void processStartSleep(Matcher m) {
        this.currentStartSleep = Integer.valueOf(m.group(1));
    }

    private void processEndSleep(Matcher m) {
        String currentDate = m.group(1);
        int currentEndSleep = Integer.valueOf(m.group(2));
        currentGuard.sleep(currentDate, currentStartSleep, currentEndSleep);
    }

    private boolean tryPattern(String line, Pattern pattern, Consumer<Matcher> processer) {
        Matcher m = pattern.matcher(line);
        if (m.matches()) {
            processer.accept(m);
            return true;
        } else {
            return false;
        }
    }
}
