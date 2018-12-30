"use strict";

let Set = require("collections/set");
let FieldWalker = require("./fieldwalker");

/*
 * A FieldWalker that collects all unique field names into the fields Set.
 */
class UniqFields extends FieldWalker {
    constructor() {
        super();
        this.fields = new Set();
    }
    
    visitPrimitive(fieldName, fieldValue) {
        this.fields.add(fieldName);
    }
}

module.exports = function(obj) {
    let uniqFields = new UniqFields();
    uniqFields.walk(obj);
    return uniqFields.fields.toArray();
};