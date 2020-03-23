package com.github.jschmidt10;

import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MergeSortIteratorTest {

    @Test
    public void shouldMergeSortMultipleIteratorsWithNoDuplication() {
        SortedSet<Integer> set1 = new TreeSet<>(Arrays.asList(1, 2));
        SortedSet<Integer> set2 = new TreeSet<>(Arrays.asList(2, 3, 4));
        SortedSet<Integer> set3 = new TreeSet<>(Arrays.asList(4, 5, 6, 7));

        MergeSortIterator<Integer> mergedIter = new MergeSortIterator<>(Comparator.naturalOrder(),
                Arrays.asList(set1.iterator(), set2.iterator(), set3.iterator()));

        int expected = 1;
        while (mergedIter.hasNext()) {
            assertThat(mergedIter.next(), is(expected++));
        }

        assertThat(expected, is(8));
    }
}
