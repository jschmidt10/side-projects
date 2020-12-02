package com.github.jschmidt10.advent.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface InputFile<T> {
  Stream<T> stream();

  default List<T> getLines() {
    return stream().collect(Collectors.toList());
  }
}
