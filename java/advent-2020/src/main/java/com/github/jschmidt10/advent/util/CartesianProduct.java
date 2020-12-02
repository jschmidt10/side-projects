package com.github.jschmidt10.advent.util;

import java.util.Iterator;
import java.util.function.BiConsumer;

public class CartesianProduct<S, T> {

  private final Iterable<S> iterable1;
  private final Iterable<T> iterable2;

  public CartesianProduct(Iterable<S> iterable1, Iterable<T> iterable2) {
    this.iterable1 = iterable1;
    this.iterable2 = iterable2;
  }

  public void apply(BiConsumer<S, T> biConsumer) {
    Iterator<S> iter1 = iterable1.iterator();

    while(iter1.hasNext()) {
      S element = iter1.next();
      Iterator<T> iter2 = iterable2.iterator();

      while (iter2.hasNext()) {
        biConsumer.accept(element, iter2.next());
      }
    }
  }
}
