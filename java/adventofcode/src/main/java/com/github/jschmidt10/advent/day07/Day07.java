package com.github.jschmidt10.advent.day07;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Day07 {
    private static char START_CHAR = 'A' - 1;
    private static int MAX_WORKERS = 5;
    private static int BASE_TIME_SECS = 60;

    public static void main(String[] args) throws IOException {
        String filename = "src/main/resources/day07_inputs.txt";
//        solvePart1(filename);
        solvePart2(filename);
    }

    private static void solvePart2(String filename) throws IOException {
        Map<String, Node> nodes = buildNodes(filename);

        SortedSet<Node> todo = new TreeSet<>();
        SortedSet<Node> ready = new TreeSet<>();

        todo.addAll(nodes.values());
        nodes.values().stream().filter(n -> n.parents.isEmpty()).forEach(ready::add);

        Set<Node> finished = new HashSet<>();

        int seconds = -1;
        Worker[] workers = new Worker[MAX_WORKERS];
        for (int i = 0; i < MAX_WORKERS; i++) {
            workers[i] = new Worker();
        }

        while (!todo.isEmpty()) {
            for (int i = 0; i < MAX_WORKERS; i++) {
                Worker worker = workers[i];
                // See if any work is done
                if (worker.isBusy && worker.timeDone <= seconds) {
                    finished.add(worker.node);
                    todo.remove(worker.node);
                    ready.addAll(isReadyToWork(worker.node.children, finished));
                    worker.isBusy = false;
                    worker.node = null;

                    // if we completed work, reset the loop, a non-busy worker may be able to
                    // pick up something now
                    i = -1;
                }

                // Now see if workers can take work
                if (!worker.isBusy && !ready.isEmpty()) {
                    Node work = ready.first();
                    ready.remove(work);

                    worker.isBusy = true;
                    worker.node = work;
                    worker.timeDone = seconds + work.time;
                }
            }
//            printState(seconds, workers, todo, ready, finished);
            seconds++;
        }

        System.out.println("Finished in " + seconds + " seconds!");
    }

    private static void printState(int seconds, Worker[] workers, SortedSet<Node> todo, SortedSet<Node> ready, Set<Node> finished) {
        System.out.println("Second " + seconds);
        System.out.println("To Do: " + todo.stream().map(n -> n.id).collect(Collectors.toList()));
        System.out.println("Ready: " + ready.stream().map(n -> n.id).collect(Collectors.toList()));
        System.out.println("Finished: " + finished.stream().map(n -> n.id).collect(Collectors.toList()));

        for (int i = 0; i < workers.length; i++) {
            System.out.println("Worker " + i + ": " + workers[i]);
        }
        System.out.println("-------------------------");
    }

    private static class Worker {
        boolean isBusy = false;
        int timeDone = 0;
        Node node;

        @Override
        public String toString() {
            return "Worker(isBusy=" + isBusy + ", timeDone=" + timeDone + ", working=" + Optional.ofNullable(node).map(n -> n.id).orElse("");
        }
    }

    private static void solvePart1(String filename) throws IOException {
        Map<String, Node> nodes = buildNodes(filename);

        SortedSet<Node> toVisit = new TreeSet<>();
        nodes.values().stream().filter(n -> n.parents.isEmpty()).forEach(toVisit::add);

        Set<Node> visited = new HashSet<>();

        while (!toVisit.isEmpty()) {
            Node n = toVisit.first();
            toVisit.remove(n);

            System.out.print(n.id);
            visited.add(n);
            toVisit.addAll(isReadyToWork(n.children, visited));
        }

        System.out.println("\nAll Done!");
    }

    private static List<Node> isReadyToWork(List<Node> tasks, Set<Node> finished) {
        return tasks.stream().filter(t -> !finished.contains(t) && finished.containsAll(t.parents)).collect(Collectors.toList());
    }

    private static Map<String, Node> buildNodes(String filename) throws IOException {
        Map<String, Node> nodes = new HashMap<>();

        Files.lines(Paths.get(filename)).forEach(line -> {
            String[] tokens = line.split(" ");
            String fromId = tokens[1];
            String toId = tokens[7];

            Node from = nodes.computeIfAbsent(fromId, k -> new Node(k));
            Node to = nodes.computeIfAbsent(toId, k -> new Node(k));

            to.parents.add(from);
            from.children.add(to);
        });
        return nodes;
    }

    private static class Node implements Comparable<Node> {
        String id;
        List<Node> parents = new ArrayList<>();
        List<Node> children = new ArrayList<>();
        int time;

        Node(String id) {
            this.id = id;
            this.time = BASE_TIME_SECS + (id.charAt(0) - START_CHAR);
        }

        @Override
        public int compareTo(Node o) {
            return id.compareTo(o.id);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Node node = (Node) o;

            return id != null ? id.equals(node.id) : node.id == null;
        }

        @Override
        public int hashCode() {
            return id != null ? id.hashCode() : 0;
        }
    }
}
