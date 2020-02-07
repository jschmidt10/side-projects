package com.github.jschmidt10.datawave.delete.impl;

import com.github.jschmidt10.datawave.delete.framework.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;

public class StageOne implements Stage<Void,Integer>, Serializable {

    @Override
    public void process(Iterator<MultiPair<Void>> readerIter, Writer<Integer> writer) {
        while (readerIter.hasNext()) {
            MultiPair<Void> inputPair = readerIter.next();
            MultiPair<Integer> outputPair = new MultiPair<>(inputPair.getRequest(), inputPair.getRequest().id.length());
            try {
                System.out.println(this + " processed " + inputPair);
                writer.write(outputPair);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
