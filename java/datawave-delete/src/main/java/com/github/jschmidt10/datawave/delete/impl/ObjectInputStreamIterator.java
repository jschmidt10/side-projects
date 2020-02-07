package com.github.jschmidt10.datawave.delete.impl;

import com.github.jschmidt10.datawave.delete.framework.MultiPair;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ObjectInputStreamIterator<V> implements Iterator<MultiPair<V>> {
    private final ObjectInputStream inputStream;
    private final InputStream underlying;

    private MultiPair<V> next = null;

    public ObjectInputStreamIterator(InputStream underlying) {
        this.underlying = underlying;
        try {
            this.inputStream = new ObjectInputStream(underlying);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasNext() {
        if (next != null) {
            return true;
        }
        try {
            if (underlying.available() > 0) {
                next = (MultiPair<V>) inputStream.readObject();
                return true;
            }
            else {
                return false;
            }
        } catch (EOFException e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MultiPair<V> next() {
        if (next == null) {
            throw new NoSuchElementException();
        }

        return next;
    }
}
