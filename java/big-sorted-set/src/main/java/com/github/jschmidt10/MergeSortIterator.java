package com.github.jschmidt10;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An Iterator that merges multiple sorted iterators together while de-duplicating.
 * @param <T>
 */
public class MergeSortIterator<T> implements Iterator<T> {

    private Comparator<T> comparator;
    private List<PeekingIterator<T>> iters;
    private T next;

    public MergeSortIterator(Comparator<T> comparator, List<Iterator<T>> iters) {
        this.comparator = comparator;
        this.iters = iters.stream().map(PeekingIterator::new).collect(Collectors.toList());
    }

    @Override
    public boolean hasNext() {
        if (next == null) {
            T min = null;

            for (PeekingIterator<T> iter : iters) {
                T peeked = iter.peek();
                if (peeked != null) {
                    if (min == null || comparator.compare(peeked, min) < 0) {
                        min = peeked;
                    }
                }
            }

            if (min != null) {
                next = min;

                for (PeekingIterator<T> iter : iters) {
                    if (iter.peek() != null && comparator.compare(next, iter.peek()) == 0) {
                        iter.next();
                    }
                }
            }
        }
        return next != null;
    }

    @Override
    public T next() {
        T result = next;
        next = null;
        return result;
    }
}
