package com.github.jschmidt10;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BigSortedSetTest {

    @Rule
    public TemporaryFolder tmpdir = new TemporaryFolder();

    private Comparator<Person> comparator;
    private BigSortedSet<Person> bigSortedSet;

    @Before
    public void setup() {
        int maxSpilledFiles = 10;
        int bufferSize = 10;
        int minFilesToCompact = 3;
        Path workDir = tmpdir.getRoot().toPath();

        comparator = Comparator.comparing(p -> p.name);

        bigSortedSet = new BigSortedSet.Builder<Person>()
                .maxSpillFiles(maxSpilledFiles)
                .bufferSize(bufferSize)
                .minFilesToCompact(minFilesToCompact)
                .workDir(workDir)
                .comparator(comparator)
                .serializer(new PersonSerializer())
                .build();
    }

    @Test
    public void shouldSortLargeInputStream() throws IOException {
        int numElements = 143;
        SortedSet<Person> seen = new TreeSet<>(comparator);

        for (int i = 0; i < numElements; i++) {
            Person p = new Person(UUID.randomUUID().toString());
            seen.add(p);
            bigSortedSet.add(p);
        }

        assertThat(bigSortedSet.size(), is(seen.size()));

        Iterator<Person> expectedIter = seen.iterator();
        Iterator<Person> actualIter = bigSortedSet.iterator();

        while (expectedIter.hasNext()) {
            assertThat(actualIter.hasNext(), is(true));
            assertThat(actualIter.next(), is(expectedIter.next()));
        }

        assertThat(actualIter.hasNext(), is(false));

        bigSortedSet.clear();

        assertThat(bigSortedSet.size(), is(0));
    }

    private static class PersonSerializer implements SFileSerializer<Person> {

        @Override
        public void write(Person person, DataOutput dataOutput) throws IOException {
            dataOutput.writeUTF(person.name);
        }

        @Override
        public Person read(DataInput dataInput) throws IOException {
            return new Person(dataInput.readUTF());
        }
    }

    private static class Person {
        public final String name;

        public Person(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return name.equals(person.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
}
