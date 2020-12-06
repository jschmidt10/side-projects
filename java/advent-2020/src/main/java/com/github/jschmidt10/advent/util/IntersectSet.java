package com.github.jschmidt10.advent.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BinaryOperator;

public class IntersectSet<T> implements BinaryOperator<Set<T>> {

  public Set<T> apply(Set<T> set1, Set<T> set2) {
    Set<T> result = new HashSet<>();

    for (T element : set1) {
      if (set2.contains(element)) {
        result.add(element);
      }
    }

    return result;
  }

  public static <T> Set<T> intersectAll(Collection<Set<T>> sets) {
    return sets
        .stream()
        .reduce(new IntersectSet<>())
        .orElseThrow(() -> new IllegalArgumentException("Must provide at least one set!"));
  }
}
