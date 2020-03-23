package com.github.jschmidt10;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

/**
 * Creates new {@link SFile}s.
 * @param <T>
 */
public final class SFileFactory<T> {

    private final SFileSerializer<T> serializer;
    private final Path workDir;

    public SFileFactory(SFileSerializer<T> serializer, Path workDir) {
        this.serializer = Objects.requireNonNull(serializer);
        this.workDir = Objects.requireNonNull(workDir);
    }

    /**
     * Creates a new ElementFile with the given number of compactions.
     * @param numCompactions The numCompactions to assign to this file
     * @param numElements The number of elements this file contains
     * @return A new element file
     */
    public SFile<T> newFile(int numCompactions, int numElements) {
        return new SFile<>(serializer, getUniquePath(workDir), numCompactions, numElements);
    }

    private Path getUniquePath(Path workDir) {
        Path path = Paths.get(workDir.toString(), UUID.randomUUID().toString());
        while (Files.exists(path)) {
            path = Paths.get(workDir.toString(), UUID.randomUUID().toString());
        }
        return path;
    }
}
