package com.github.jschmidt10.advent.day07;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Bag implements Comparable<Bag> {
  public final String color;
  private Set<BagSpec> children;
  private Set<Bag> parents;

  public Bag(String color) {
    this.color = color;
    this.children = new HashSet<>();
    this.parents = new HashSet<>();
  }

  public void addChild(BagSpec child) {
    this.children.add(child);
  }

  public void addParent(Bag parent) {
    this.parents.add(parent);
  }

  public Set<Bag> getParents() {
    return Collections.unmodifiableSet(parents);
  }

  public int getTotalBags() {
    int total = 1;

    for (BagSpec childSpec : children) {
      total += (childSpec.quantity * childSpec.bag.getTotalBags());
    }

    return total;
  }

  @Override
  public int compareTo(Bag o) {
    return color.compareTo(o.color);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Bag bag = (Bag) o;
    return color.equals(bag.color);
  }

  @Override
  public int hashCode() {
    return Objects.hash(color);
  }

  @Override
  public String toString() {
    return "Bag{" +
        "color='" + color + '\'' +
        ", children=" + children +
        ", parents=" + parents.stream().map(bag -> bag.color).collect(Collectors.joining(",")) +
        '}';
  }
}
