package com.github.jschmidt10.advent.day07;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BagDefinition {
  private final String bagColor;
  private final Map<String, Integer> childrenToQuantity;

  public BagDefinition(String bagColor,
      Map<String, Integer> childrenToQuantity) {
    this.bagColor = bagColor;
    this.childrenToQuantity = childrenToQuantity;
  }

  public String getBagColor() {
    return bagColor;
  }

  public Map<String, Integer> getChildrenToQuantity() {
    return Collections.unmodifiableMap(childrenToQuantity);
  }

  public static class Builder {
    private String bagColor;
    private Map<String, Integer> childrenToQuantity;

    public Builder() {
      this.childrenToQuantity = new HashMap<>();
    }

    public void setBagColor(String bagColor) {
      this.bagColor = bagColor;
    }

    public void addChild(String childColor, int quantity) {
      childrenToQuantity.put(childColor, quantity);
    }

    public BagDefinition build() {
      return new BagDefinition(bagColor, childrenToQuantity);
    }
  }
}
