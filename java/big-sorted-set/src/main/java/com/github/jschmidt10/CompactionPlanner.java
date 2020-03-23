package com.github.jschmidt10;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Computes Compaction plans for a given set of files.
 * <p>
 * Each ElementFile has an associated 'numCompactions' field which denotes how many times the elements
 * inside have undergone a compaction. The goal is to minimize the number of times we rewrite an element
 * into new files.
 * <p>
 * This algorithm will sort the files by their 'numCompactions'. It will then take 'minFilesToCompact' files
 * from that set. All files with the same 'numCompactions' will be compacted together.
 * <p>
 * Examples:
 * X(#) means File X has been compacted # of times.
 * <p>
 * Case 1: minFilesToCompact=3, A(1), B(1), C(1), D(1), E(2)
 * A-D will be compacted. We chose the 3 minimum files (A-C) but then group all files that have been compacted once.
 * <p>
 * Case 2: minFilesToCompact=3 A(1), B(2), C(2), D(2), E(3)
 * A-D will be compacted. We chose the 3 minimum files (A-C) but then group all files that have been compacted once or twice.
 */
public class CompactionPlanner {

    private final int minFilesToCompact;

    public CompactionPlanner(int minFilesToCompact) {
        this.minFilesToCompact = Arguments.greaterThanZero(minFilesToCompact);
    }

    /**
     * Gets the ElementFiles to compact.
     *
     * @param files The element files.
     * @return The files that should be compacted
     */
    public <T> List<SFile<T>> compute(List<SFile<T>> files) {
        List<Integer> sortedCompactionNums = sortedCompactionNums(files);
        Set<Integer> distinctCompactionNums = takeUnique(sortedCompactionNums, minFilesToCompact);

        return filterFiles(files, distinctCompactionNums);
    }

    private <T> List<SFile<T>> filterFiles(List<SFile<T>> files, Set<Integer> distinctCompactionNums) {
        return files
                .stream()
                .filter(ef -> distinctCompactionNums.contains(ef.getNumCompactions()))
                .collect(Collectors.toList());
    }

    private <T> List<Integer> sortedCompactionNums(List<SFile<T>> files) {
        return files
                .stream()
                .map(SFile::getNumCompactions)
                .sorted()
                .collect(Collectors.toList());
    }

    private Set<Integer> takeUnique(List<Integer> ints, int numElements) {
        return ints
                .stream()
                .limit(numElements)
                .collect(Collectors.toSet());
    }
}
