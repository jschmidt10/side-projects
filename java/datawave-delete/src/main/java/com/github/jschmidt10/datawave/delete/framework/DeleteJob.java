package com.github.jschmidt10.datawave.delete.framework;

import java.util.Collection;

public class DeleteJob {
    public final Collection<DeleteRequest> requests;

    public DeleteJob(Collection<DeleteRequest> requests) {
        this.requests = requests;
    }
}
