package com.github.jschmidt10.advent.util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

public class IntInputFile implements InputFile<Integer> {

  private final StringInputFile stringInputFile;

  public IntInputFile(String inputPath) {
    this(Paths.get(inputPath));
  }

  public IntInputFile(Path inputPath) {
    this.stringInputFile = new StringInputFile(inputPath);
  }

  @Override
  public Stream<Integer> stream() {
    return stringInputFile.stream().map(Integer::parseInt);
  }

  public SortedSet<Integer> sort() {
    SortedSet<Integer> sorted = new TreeSet<>();
    stream().forEach(sorted::add);
    return sorted;
  }
}
