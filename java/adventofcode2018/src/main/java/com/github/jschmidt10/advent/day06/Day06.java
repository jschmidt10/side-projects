package com.github.jschmidt10.advent.day06;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Day06 {

    public static void main(String[] args) throws IOException {
        String filename = "src/main/resources/day06_inputs.txt";
//        solvePart1(filename);
        solvePart2(filename);
    }

    private static void solvePart2(String filename) throws IOException {
        List<Point> points = Files.lines(Paths.get(filename)).map(Day06::parsePoint).collect(Collectors.toList());
        Point maxDims = findMaxDimensions(points);

        int closeEnoughRegionSize = 0;
        for (int i = 0; i <= maxDims.x; i++) {
            for (int j = 0; j <= maxDims.y; j++) {
                Point thisPoint = new Point(i, j);
                int distanceToAllPoints = points.stream().map(p -> p.distance(thisPoint)).mapToInt(Integer::intValue).sum();
                if (distanceToAllPoints < 10000) {
                    closeEnoughRegionSize++;
                }
            }
        }

        System.out.println("Close enough region: " + closeEnoughRegionSize);
    }

    private static int getDistanceToAllPoints(Point point, List<Point> points) {
        int distance = 0;

        for (Point other : points) {
            distance += other.distance(point);
        }

        return distance;
    }

    private static void solvePart1(String filename) throws IOException {
        List<Point> points = Files.lines(Paths.get(filename)).map(Day06::parsePoint).collect(Collectors.toList());
        BoardSpot[][] board = createBoard(findMaxDimensions(points));

        // for each point, crawl the board and update the closest point
        for (Point p : points) {
            crawl(board, p);
        }

        // Now assign region areas/isInfinite
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                BoardSpot spot = board[i][j];
                if (spot.owner != null) {
                    if (i == 0 || (i == board.length - 1) || j == 0 || (j == board[i].length - 1)) {
                        spot.owner.isInfinite = true;
                        spot.owner.size = -1;
                    } else {
                        spot.owner.size++;
                    }
                }
            }
        }

        // Now find max non-infinte region size
        Point biggestRegion = points.stream().filter(p -> !p.isInfinite).max(Comparator.comparing(p -> p.size)).get();
        System.out.println(biggestRegion.size);
    }

    private static BoardSpot[][] createBoard(Point maxDims) {
        BoardSpot[][] board = new BoardSpot[maxDims.x + 1][maxDims.y + 1];

        // initialize empty board
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new BoardSpot();
            }
        }
        return board;
    }

    private static void printBoard(BoardSpot[][] board) {
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length; j++) {
                BoardSpot spot = board[j][i];
                if (spot.owner == null) {
                    System.out.print(". ");
                } else {
                    System.out.print(spot.owner.id + " ");
                }
            }
            System.out.println();
        }
    }

    private static void printPoint(Point point) {
        System.out.println("Point " + point.id + ": (" + point.x + ", " + point.y + "), isInfinite = " + point.isInfinite + ", size = " + point.size);
    }

    private static void crawl(BoardSpot[][] board, Point p) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                BoardSpot spot = board[i][j];
                int distance = new Point(i, j).distance(p);
                if (!spot.visited) {
                    spot.owner = p;
                    spot.distance = distance;
                    spot.visited = true;
                } else if (distance == spot.distance) {
                    spot.owner = null; // tie, no one owns it
                } else if (distance < spot.distance) {
                    spot.distance = distance;
                    spot.owner = p;
                }
            }
        }
    }

    private static Point findMaxDimensions(List<Point> points) {
        Point maxDimensions = new Point(0, 0);

        for (Point point : points) {
            if (maxDimensions.x < point.x) {
                maxDimensions.x = point.x;
            }
            if (maxDimensions.y < point.y) {
                maxDimensions.y = point.y;
            }
        }

        return maxDimensions;
    }

    private static Point parsePoint(String line) {
        String[] tokens = line.split(",");
        return new Point(Integer.valueOf(tokens[0].trim()), Integer.valueOf(tokens[1].trim()));
    }
}
