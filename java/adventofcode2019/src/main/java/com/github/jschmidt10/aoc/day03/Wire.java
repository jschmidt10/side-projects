package com.github.jschmidt10.aoc.day03;

public class Wire {

    public final int id;

    private int numSteps = 0;
    private Point tail;

    public Wire(int id, Point tail) {
        this.id = id;
        this.tail = tail;
    }

    public Point getTail() {
        return tail;
    }

    public void append(Point p) {
        numSteps++;
        tail = p;
    }

    public int getNumSteps() {
        return numSteps;
    }
}
