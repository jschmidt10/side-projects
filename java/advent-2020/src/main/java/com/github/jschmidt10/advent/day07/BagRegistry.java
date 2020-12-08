package com.github.jschmidt10.advent.day07;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BagRegistry {
  private Map<String, Bag> bags;
  private BagDefinitionParser parser;

  public BagRegistry() {
    this(new BagDefinitionParser());
  }

  public BagRegistry(BagDefinitionParser parser) {
    this.bags = new HashMap<>();
    this.parser = parser;
  }

  public Bag registerBag(String line) {
    BagDefinition bagDefinition = parser.parseBagDefinition(line);

    Bag bag = getBag(bagDefinition.getBagColor());

    for (Entry<String, Integer> childEntry : bagDefinition.getChildrenToQuantity().entrySet()) {
      Bag child = getBag(childEntry.getKey());
      BagSpec childSpec = new BagSpec(child, childEntry.getValue());

      bag.addChild(childSpec);
      child.addParent(bag);
    }

    return bag;
  }

  public Bag getBag(String color) {
    Bag bag = bags.get(color);
    if (bag == null) {
      bag = new Bag(color);
      bags.put(color, bag);
    }

    return bag;
  }
}
