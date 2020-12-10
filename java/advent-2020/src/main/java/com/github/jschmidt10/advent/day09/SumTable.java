package com.github.jschmidt10.advent.day09;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class SumTable {
  private SortedSet<Long> members = new TreeSet<>();
  private Map<Long, Long> sumsAndCounts = new HashMap<>();

  public void add(long member) {
    for (long other : members) {
      long sum = other + member;
      long count = sumsAndCounts.getOrDefault(sum, 0L) + 1;
      sumsAndCounts.put(sum, count);
    }
    members.add(member);
  }

  public void remove(long member) {
    if (!members.remove(member)) {
      throw new IllegalArgumentException(member + " was not a member!");
    }

    for (long other : members) {
      long sum = other + member;
      long count = sumsAndCounts.get(sum) - 1;

      if (count == 0) {
        sumsAndCounts.remove(sum);
      }
      else {
        sumsAndCounts.put(sum, count);
      }
    }
  }

  public boolean isPossibleSum(long sum) {
    return sumsAndCounts.containsKey(sum);
  }
}
