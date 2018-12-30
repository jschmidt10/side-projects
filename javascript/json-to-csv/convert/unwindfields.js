"use strict";

let Set = require("collections/set");
let FieldWalker = require("./fieldwalker");

class UnwindFields extends FieldWalker {
    constructor() {
        super();
        this.fields = new Set();
    }

    visitArray(fieldName, list) {
        this.fields.add(fieldName);
    }
}

module.exports = function(obj) {
  let unwindFields = new UnwindFields();
  unwindFields.walk(obj);
  return unwindFields.fields.toArray();
};