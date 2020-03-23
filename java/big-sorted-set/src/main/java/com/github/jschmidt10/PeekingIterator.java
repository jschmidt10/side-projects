package com.github.jschmidt10;

import java.util.Iterator;

/**
 * An Iterator that can peek the top element without advancing the iterator.
 * @param <T>
 */
public class PeekingIterator<T> implements Iterator<T> {

    private Iterator<T> inner;
    private T peeked;

    public PeekingIterator(Iterator<T> inner) {
        this.inner = inner;
    }

    /**
     * Peeks at the top element without advancing the iterator.
     * @return The top element or null if no more elements exist.
     */
    public T peek() {
        hasNext();
        return peeked;
    }

    @Override
    public boolean hasNext() {
        if (peeked == null && inner.hasNext()) {
            peeked = inner.next();
        }
        return peeked != null;
    }

    @Override
    public T next() {
        T result = peeked;
        peeked = null;
        return result;
    }
}
