package com.github.jschmidt10.aoc.day03;

import java.util.ArrayList;
import java.util.List;

public class WireStep {

    public final char direction;
    public final int dist;

    public WireStep(char direction, int dist) {
        this.direction = direction;
        this.dist = dist;
    }

    public static WireStep fromStr(String s) {
        char dir = s.charAt(0);
        int dist = Integer.parseInt(s.substring(1));
        return new WireStep(dir, dist);
    }

    public List<Point> apply(Wire wire) {
        Point start = wire.getTail();
        List<Point> points = new ArrayList<>();

        switch (direction) {
            case 'U':
                for (int dy = 1; dy <= dist; dy++) {
                    points.add(new Point(start.x, start.y + dy));
                }
                break;
            case 'D':
                for (int dy = 1; dy <= dist; dy++) {
                    points.add(new Point(start.x, start.y - dy));
                }
                break;
            case 'L':
                for (int dx = 1; dx <= dist; dx++) {
                    points.add(new Point(start.x - dx, start.y));
                }
                break;
            case 'R':
                for (int dx = 1; dx <= dist; dx++) {
                    points.add(new Point(start.x + dx, start.y));
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown direction: " + direction);
        }

        return points;
    }
}
