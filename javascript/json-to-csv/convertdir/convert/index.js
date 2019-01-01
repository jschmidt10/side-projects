"use strict";

const json2csv = require("json2csv");

const FieldWalker = require("./fieldwalker");
const UniqueFieldVisitor = require("./unique-field-visitor");
const UnwindFieldVisitor = require("./unwind-field-visitor");

module.exports = function (json) {
    let obj = JSON.parse(json);

    let fw = new FieldWalker();
    let uniqFieldVisitor = new UniqueFieldVisitor();
    let unwindFieldVisitor = new UnwindFieldVisitor();

    fw.walk(obj, [ uniqFieldVisitor, unwindFieldVisitor ]);

    return json2csv.parse(obj,
        {
            fields: uniqFieldVisitor.fields,
            flatten: true,
            unwind: unwindFieldVisitor.fields
        });
};