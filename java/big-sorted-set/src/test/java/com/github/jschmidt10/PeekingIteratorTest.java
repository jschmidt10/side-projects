package com.github.jschmidt10;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PeekingIteratorTest {

    @Test
    public void shouldPeek() {
        List<Integer> nums = Arrays.asList(1, 2, 3);
        PeekingIterator<Integer> peekingIterator = new PeekingIterator<>(nums.iterator());

        assertThat(peekingIterator.peek(), is(1));
        assertThat(peekingIterator.peek(), is(1));
        assertThat(peekingIterator.next(), is(1));

        assertThat(peekingIterator.hasNext(), is(true));
        assertThat(peekingIterator.next(), is(2));

        assertThat(peekingIterator.hasNext(), is(true));
        assertThat(peekingIterator.peek(), is(3));
        assertThat(peekingIterator.peek(), is(3));
        assertThat(peekingIterator.next(), is(3));

        assertThat(peekingIterator.hasNext(), is(false));
    }
}
