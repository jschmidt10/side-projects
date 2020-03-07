package com.github.jschmidt10.advent.day06;

public class Point {

    private static char nextChar = 'A';

    char id = nextChar++;
    int x;
    int y;
    boolean isInfinite = false;
    int size = 0;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    int distance(Point p) {
        return Math.abs(p.x - x) + Math.abs(p.y - y);
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (x != point.x) return false;
        return y == point.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
