package com.github.jschmidt10.advent.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringInputFile implements InputFile<String> {

  private final List<String> lines;

  public StringInputFile(String inputPath) {
    this(Paths.get(inputPath));
  }
  public StringInputFile(Path path) {
    this.lines = readLines(path);
  }

  private List<String> readLines(Path path) {
    try (Stream<String> lines = Files.lines(path)) {
      return lines.collect(Collectors.toList());
    } catch (IOException e) {
      throw new RuntimeException("Failed to read " + path, e);
    }
  }

  @Override
  public Stream<String> stream() {
    return lines.stream();
  }

  public List<List<String>> getGroupedLines() {
    List<List<String>> groupedLines = new ArrayList<>();

    List<String> currentGroup = new ArrayList<>();

    for (String line : getLines()) {
      if (isGroupComplete(line)) {
        groupedLines.add(currentGroup);
        currentGroup = new ArrayList<>();
      }
      else {
        currentGroup.add(line);
      }
    }

    if (!currentGroup.isEmpty()) {
      groupedLines.add(currentGroup);
    }

    return groupedLines;
  }

  private boolean isGroupComplete(String line) {
    return line.isBlank();
  }
}
