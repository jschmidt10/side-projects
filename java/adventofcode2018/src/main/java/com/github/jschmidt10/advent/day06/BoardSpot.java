package com.github.jschmidt10.advent.day06;

public class BoardSpot {
    int distance;
    Point owner;
    boolean visited = false;
    boolean isCloseEnough = false;

    @Override
    public String toString() {
        return "[" + owner + "," + distance + "]";
    }
}
