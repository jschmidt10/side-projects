package com.github.jschmidt10.advent.util;

import java.util.stream.Stream;

public interface InputFile<T> {
  Stream<T> stream();
}
