package com.github.jschmidt10.datawave.delete.impl;

import com.github.jschmidt10.datawave.delete.framework.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

public class StageTwo implements Stage<Integer,Integer>, Serializable {
    @Override
    public void process(Iterator<MultiPair<Integer>> readerIter, Writer<Integer> writer) {
        while (readerIter.hasNext()) {
            MultiPair<Integer> inputPair = readerIter.next();

            Collection<Integer> doubles = inputPair.getValues().stream().map(x -> x * 2).collect(Collectors.toList());
            MultiPair<Integer> outputPair = new MultiPair<>(inputPair.getRequest(), doubles);

            try {
                System.out.println(this + " processed " + inputPair);
                writer.write(outputPair);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
