package com.github.jschmidt10.aoc.day06;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class DaySix {
    public static void main(String[] args) throws IOException {
        String filename = "data/day6.txt";

        OrbitMap orbitMap = new OrbitMap();

        try (Stream<String> lines = Files.lines(Paths.get(filename))) {
            lines.forEach(line -> {
                String[] tokens = line.split("\\)");
                orbitMap.addOrbit(tokens[0], tokens[1]);
            });

            System.out.println("part1: " + orbitMap.countOrbitsFrom(OrbitMap.COM));

            orbitMap.clearDistances();
            orbitMap.countOrbitsFrom("YOU");
            System.out.println("part2: " + (orbitMap.getNode("SAN").getDist() - 2));
        }
    }

    public static class OrbitMap {
        public static final String COM = "COM";

        private Node com;
        private Map<String, Node> nodeLookup;

        public OrbitMap() {
            com = new Node(COM);

            nodeLookup = new HashMap<>();
            nodeLookup.put(com.name, com);
        }

        public Node getNode(String name) {
            return nodeLookup.get(name);
        }

        public void addOrbit(String orbitteeName, String orbitterName) {
            Node orbittee = nodeLookup.computeIfAbsent(orbitteeName, Node::new);
            Node orbitter = nodeLookup.computeIfAbsent(orbitterName, Node::new);

            orbittee.addChild(orbitter);
            orbitter.setParent(orbittee);
        }

        public int countOrbitsFrom(String nodeName) {
            assignDistances(nodeName);

            int sum = 0;
            for (Node n : nodeLookup.values()) {
                sum += n.getDist();
            }

            return sum;
        }

        private void assignDistances(String nodeName) {
            int distFromCom = 0;

            List<Node> curTier = new ArrayList<>();
            curTier.add(nodeLookup.get(nodeName));

            List<Node> nextTier = new ArrayList<>();
            Set<Node> visited = new HashSet<>();

            while (!curTier.isEmpty()) {
                for (Node n : curTier) {
                    visited.add(n);
                    n.setDist(distFromCom);

                    if (n.getParent() != null && !visited.contains(n.getParent())) {
                        nextTier.add(n.getParent());
                    }

                    for (Node child : n.getChildren()) {
                        if (!visited.contains(child)) {
                            nextTier.add(child);
                        }
                    }
                }

                curTier = nextTier;
                nextTier = new ArrayList<>();
                distFromCom++;
            }
        }

        public void clearDistances() {
            for (Node n : nodeLookup.values()) {
                n.setDist(0);
            }
        }
    }

    public static class Node {
        private final String name;

        private Node parent;
        private List<Node> children;
        private int dist = 0;

        public Node(String name) {
            this.name = name;
            this.children = new ArrayList<>();
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public void addChild(Node child) {
            children.add(child);
        }

        public List<Node> getChildren() {
            return children;
        }

        public void setDist(int dist) {
            this.dist = dist;
        }

        public int getDist() {
            return dist;
        }
    }
}
