package com.github.jschmidt10.advent.day07;

public class BagSpec {
  public final Bag bag;
  public final int quantity;

  public BagSpec(Bag bag, int quantity) {
    this.bag = bag;
    this.quantity = quantity;
  }

  @Override
  public String toString() {
    return "BagSpec{" +
        "bag=" + bag.color +
        ", quantity=" + quantity +
        '}';
  }
}
