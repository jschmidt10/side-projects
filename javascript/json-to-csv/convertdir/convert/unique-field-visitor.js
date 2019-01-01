"use strict";

let Set = require("collections/set");

/*
 * A fieldwalker visitor that accumulates unique field names.
 */
class UniqFieldVisitor {
    constructor() {
        this.fields = new Set();
    }
    
    visitPrimitive(fieldName, fieldValue) {
        this.fields.add(fieldName);
    }

    done() {
        // convert Set to an Array
        this.fields = this.fields.toArray();
    }
}

module.exports = UniqFieldVisitor;