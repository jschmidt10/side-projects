package com.github.jschmidt10;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CompactionPlannerTest {

    @Rule
    public TemporaryFolder tmpDir = new TemporaryFolder();

    private int minFilesToCompact = 3;
    private CompactionPlanner compactionPlanner;

    @Before
    public void setup() {
        compactionPlanner = new CompactionPlanner(minFilesToCompact);
    }

    @Test
    public void shouldCompactFilesWithSameCompactionCount() {
        List<SFile<Integer>> files = generateFiles(0, 0, 0, 0, 1, 1, 2, 2);
        assertCompactionPlan(compactionPlanner.compute(files), 0, 0, 0, 0);
    }

    @Test
    public void shouldCompactAllFilesWhenEqualToMin() {
        List<SFile<Integer>> files = generateFiles(0, 1, 2);
        assertCompactionPlan(compactionPlanner.compute(files), 0, 1, 2);
    }

    @Test
    public void shouldCompactFilesWithHigherCounts() {
        List<SFile<Integer>> files = generateFiles(0, 1, 1, 2, 2);
        assertCompactionPlan(compactionPlanner.compute(files), 0, 1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRequireMinFilesGreaterThanZero() {
        compactionPlanner = new CompactionPlanner(0);
    }

    private List<SFile<Integer>> generateFiles(Integer... compactionCounts) {
        List<SFile<Integer>> files = new ArrayList<>();

        for (Integer compactionCount : compactionCounts) {
            files.add(new SFile<>(null, tmpDir.getRoot().toPath(), compactionCount, 0));
        }

        return files;
    }

    private void assertCompactionPlan(List<SFile<Integer>> toCompact, Integer... compactionCounts) {
        List<Integer> expected = Arrays.asList(compactionCounts);

        int i = 0;
        for (SFile<Integer> file : toCompact) {
            assertThat(file.getNumCompactions(), is(expected.get(i++)));
        }
    }
}
