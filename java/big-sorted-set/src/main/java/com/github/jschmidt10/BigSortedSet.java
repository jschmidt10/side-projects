package com.github.jschmidt10;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A SortedSet that persists files to disk in order to exceed what can fit in the heap.
 *
 * @param <T>
 */
public class BigSortedSet<T> {

    private final int maxSpillFiles;
    private final int bufferSize;
    private final SFileFactory<T> fileFactory;
    private final CompactionPlanner compactionPlanner;

    private final Comparator<T> comparator;
    private final SortedSet<T> buffer;
    private final List<SFile<T>> spilledFiles;

    public BigSortedSet(int maxSpillFiles, int bufferSize, CompactionPlanner compactionPlanner, Comparator<T> comparator, SFileFactory<T> fileFactory) {
        this.maxSpillFiles = Arguments.greaterThanZero(maxSpillFiles);
        this.bufferSize = Arguments.greaterThanZero(bufferSize);
        this.fileFactory = Objects.requireNonNull(fileFactory);
        this.compactionPlanner = Objects.requireNonNull(compactionPlanner);

        this.comparator = Objects.requireNonNull(comparator);
        this.buffer = new TreeSet<>(comparator);
        this.spilledFiles = new ArrayList<>(maxSpillFiles);
    }

    /**
     * Gets the sum of elements in all files plus the size of the memory buffer.
     *
     * Note: This number may include duplicates across files. The number could go down
     * after compaction.
     *
     * @return The total size of this set.
     */
    public int size() {
        int filesSize = spilledFiles.stream().mapToInt(SFile::getNumElements).sum();
        return filesSize + buffer.size();
    }

    /**
     * @return An iterator over this set
     */
    public Iterator<T> iterator() {
        List<Iterator<T>> iters = new ArrayList<>();

        for (SFile<T> file : spilledFiles) {
            iters.add(file.newFileIterator());
        }

        iters.add(buffer.iterator());

        return new MergeSortIterator<>(comparator, iters);
    }

    /**
     * Add an element
     * @param element The element to add
     * @throws IOException If an error occurs while spilling to disk or merging on disk files
     */
    public void add(T element) throws IOException {
        if (isBufferFull()) {
            if (needsCompaction()) {
                compact();
            }
            spillBuffer();
        }

        buffer.add(element);
    }

    private boolean isBufferFull() {
        return buffer.size() >= bufferSize;
    }

    private boolean needsCompaction() {
        return spilledFiles.size() >= maxSpillFiles;
    }

    private void compact() throws IOException {
        List<SFile<T>> compacting = compactionPlanner.compute(spilledFiles);
        SFile<T> resultFile = createResultFile(compacting);

        try (SFile<T>.Writer writer = resultFile.newWriter()) {
            MergeSortIterator<T> iterator = new MergeSortIterator<>(comparator, asIters(compacting));
            while (iterator.hasNext()) {
                writer.write(iterator.next());
            }
        }

        spilledFiles.add(resultFile);

        for (SFile<T> file : compacting) {
            file.delete();
            spilledFiles.remove(file);
        }
    }

    private SFile<T> createResultFile(List<SFile<T>> files) {
        int maxCompactions = files
                .stream()
                .mapToInt(SFile::getNumCompactions)
                .max()
                .orElse(0);

        int numElements = files
                .stream()
                .mapToInt(SFile::getNumElements)
                .sum();

        return fileFactory.newFile(maxCompactions + 1, numElements);
    }

    private List<Iterator<T>> asIters(List<SFile<T>> files) {
        return files
                .stream()
                .map(SFile::newFileIterator)
                .collect(Collectors.toList());
    }

    private void spillBuffer() throws IOException {
        SFile<T> sFile = fileFactory.newFile(0, buffer.size());
        spilledFiles.add(sFile);

        sFile.writeAll(buffer);
        buffer.clear();
    }

    /**
     * Clears out this set including any SFiles that were written.
     */
    public void clear() throws IOException {
        buffer.clear();

        IOException lastException = null;
        for (SFile<T> file : spilledFiles) {
            try {
                file.delete();
            }
            catch (IOException e) {
                lastException = e;
            }
        }

        spilledFiles.clear();

        if (lastException != null) {
            throw lastException;
        }
    }

    public static class Builder<T> {
        private int maxSpillFiles = 10;
        private int bufferSize = 2 ^ 20;
        private int minFilesToCompact = 3;

        private Path workDir;
        private Comparator<T> comparator;
        private SFileSerializer<T> serializer;

        public Builder<T> maxSpillFiles(int maxSpillFiles) {
            this.maxSpillFiles = maxSpillFiles;
            return this;
        }

        public Builder<T> bufferSize(int bufferSize) {
            this.bufferSize = bufferSize;
            return this;
        }

        public Builder<T> minFilesToCompact(int minFilesToCompact) {
            this.minFilesToCompact = minFilesToCompact;
            return this;
        }

        public Builder<T> workDir(Path workDir) {
            this.workDir = workDir;
            return this;
        }

        public Builder<T> comparator(Comparator<T> comparator) {
            this.comparator = comparator;
            return this;
        }

        public Builder<T> serializer(SFileSerializer<T> serializer) {
            this.serializer = serializer;
            return this;
        }

        public BigSortedSet<T> build() {
            CompactionPlanner compactionPlanner = new CompactionPlanner(minFilesToCompact);
            SFileFactory<T> fileFactory = new SFileFactory<>(serializer, workDir);
            return new BigSortedSet<>(maxSpillFiles, bufferSize, compactionPlanner, comparator, fileFactory);
        }
    }
}
