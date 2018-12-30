"use strict";

const Set = require("collections/set");

/*
 * Walks an object tree and produces a list of all fields in the structure.
 * Nested fields will be prefixed with their parent object name.
 */
module.exports = class FieldWalker {
    walk(obj) {
        this.walkIter("", obj);
    }

    walkIter(prefix, obj) {
        if (typeof(obj) !== "object") {
            if (this.visitPrimitive) {
                this.visitPrimitive(prefix, obj);
            }
        }
        else if (Array.isArray(obj)) {
            for (let i = 0; i < obj.length; i++) {
                if (this.visitArray) {
                    this.visitArray(prefix, obj);
                }
                this.walkIter(prefix, obj[i]);
            }
        }
        else {
            for (var property in obj) {
                if (this.visitObject) {
                    this.visitObject(prefix, obj);
                }
                let nextprefix = (prefix.length === 0) ? property : prefix + "." + property;
                this.walkIter(nextprefix, obj[property]);
            }
        }
    }
};