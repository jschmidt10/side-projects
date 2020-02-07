package com.github.jschmidt10.datawave.delete.framework;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

public class RequestReaderFactory<IN> implements ReaderFactory<IN>, Serializable {

    private final IN NOTHING = null;

    private final Collection<DeleteRequest> requests;

    public RequestReaderFactory(DeleteJob job) {
        this.requests = job.requests;
    }

    public Collection<DeleteRequest> getRequests() {
        return requests;
    }

    @Override
    public Reader<IN> newReader() {
        return new Reader<>() {
            @Override
            public void close() {

            }

            @Override
            public Iterator<MultiPair<IN>> iterator() {
                return requests.stream().map(request -> new MultiPair<>(request, NOTHING)).iterator();
            }
        };
    }
}
