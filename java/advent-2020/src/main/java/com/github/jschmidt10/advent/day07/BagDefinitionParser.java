package com.github.jschmidt10.advent.day07;

import com.github.jschmidt10.advent.day07.BagDefinition.Builder;
import java.util.Arrays;
import java.util.Iterator;

public class BagDefinitionParser {

  public BagDefinition parseBagDefinition(String line) {
    BagDefinition.Builder builder = new Builder();

    Iterator<String> tokenIter = tokenIter(line);

    builder.setBagColor(readColor(tokenIter));
    expectToken(tokenIter, "bags");
    expectTokenStartsWith(tokenIter, "contain");

    Integer quantity = readOptionalInt(tokenIter);

    if (quantity != null) {
      do {
        String childColor = readColor(tokenIter);
        builder.addChild(childColor, quantity);
        expectTokenStartsWith(tokenIter, "bag");

        quantity = readOptionalInt(tokenIter);

      } while (quantity != null);
    }

    return builder.build();
  }

  private Iterator<String> tokenIter(String line) {
    String[] tokens = line.split(" ");
    return Arrays.stream(tokens).iterator();
  }

  private String readColor(Iterator<String> tokenIter) {
    return tokenIter.next() + ' ' + tokenIter.next();
  }

  private void expectToken(Iterator<String> tokenIter, String expectToken) {
    String actualToken = tokenIter.next();
    if (!actualToken.equals(expectToken)) {
      throw new IllegalStateException("Expected token: " + expectToken + ", Actual token: " + actualToken);
    }
  }

  private void expectTokenStartsWith(Iterator<String> tokenIter, String expectedPrefix) {
    String actualToken = tokenIter.next();
    if (!actualToken.startsWith(expectedPrefix)) {
      throw new IllegalStateException("Expected token to start with: " + expectedPrefix + ", Actual token: " + actualToken);
    }
  }

  private Integer readOptionalInt(Iterator<String> tokenIter) {
    if (!tokenIter.hasNext()) {
      return null;
    }

    try {
      return Integer.parseInt(tokenIter.next());
    } catch (NumberFormatException e) {
      return null;
    }
  }
}
