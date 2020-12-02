package com.github.jschmidt10.advent.util;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class ZipStream<S, T> {

  private final Stream<S> stream1;
  private final Stream<T> stream2;

  public ZipStream(Stream<S> stream1, Stream<T> stream2) {
    this.stream1 = stream1;
    this.stream2 = stream2;
  }

  public void zip(BiConsumer<S, T> biConsumer) {
    Iterator<S> iter1 = stream1.iterator();
    Iterator<T> iter2 = stream2.iterator();

    checkLength(iter1, iter2);

    while (iter1.hasNext() && iter2.hasNext()) {
      biConsumer.accept(iter1.next(), iter2.next());
    }

    checkLength(iter1, iter2);
  }

  private void checkLength(Iterator<S> iter1, Iterator<T> iter2) {
    if (iter1.hasNext() != iter2.hasNext()) {
      throw new IllegalArgumentException("You can only zip two streams with the same size!");
    }
  }
}
