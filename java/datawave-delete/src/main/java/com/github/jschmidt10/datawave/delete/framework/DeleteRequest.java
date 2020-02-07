package com.github.jschmidt10.datawave.delete.framework;

import java.io.Serializable;

public class DeleteRequest implements Serializable {
    public final String field;
    public final String id;

    public DeleteRequest(String field, String id) {
        this.field = field;
        this.id = id;
    }

    @Override
    public String toString() {
        return "DeleteRequest{" +
                "field='" + field + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
