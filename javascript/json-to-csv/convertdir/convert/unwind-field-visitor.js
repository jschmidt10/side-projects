"use strict";

let Set = require("collections/set");
let FieldWalker = require("./fieldwalker");

/*
 * A field walker visitor that identifies which fields need unwinding. This visitor identifies all array fields.
 */
class UnwindFieldVisitor {
    constructor() {
        this.fields = new Set();
    }

    visitArray(fieldName, list) {
        this.fields.add(fieldName);
    }

    done() {
        // Converts a Set into an Array
        this.fields = this.fields.toArray();
    }
}

module.exports = UnwindFieldVisitor;