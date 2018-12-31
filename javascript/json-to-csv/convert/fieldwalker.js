"use strict";

const Set = require("collections/set");

/*
 * Walks an object tree and produces a name for each field that is accumulated using all parent object names.
 * Applies the given visitors as the tree is walked.
 * 
 * Expected visit methods are: visitPrimitive, visitArray, visitObject, and done.
 */
module.exports = class FieldWalker {
    walk(obj, visitors) {
        this.walkIter("", obj, visitors);
        this.applyVisitors(visitors, "done");
    }

    walkIter(field, obj, visitors) {
        if (typeof(obj) !== "object") {
            this.applyVisitors(visitors, "visitPrimitive", field, obj);
        }
        else if (Array.isArray(obj)) {
            for (let i = 0; i < obj.length; i++) {
                this.applyVisitors(visitors, "visitArray", field, obj);
                this.walkIter(field, obj[i], visitors);
            }
        }
        else {
            for (var property in obj) {
                this.applyVisitors(visitors, "visitObject", field, obj);
                let nextprefix = (field.length === 0) ? property : field + "." + property;
                this.walkIter(nextprefix, obj[property], visitors);
            }
        }
    }

    applyVisitors(visitors, visitMethod, ...rest) {
        for (let i = 0; i < visitors.length; i++) {
            let visitor = visitors[i];
            let method = visitor[visitMethod];
            if (method) {
                method.apply(visitor, rest);
            }
        }
    }
};