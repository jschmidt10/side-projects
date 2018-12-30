"use strict";

const json2csv = require("json2csv");
const uniqFields = require("./uniqfields");
const unwindFields = require("./unwindfields");

module.exports = function (json) {
    let obj = JSON.parse(json);
    return json2csv.parse(obj,
        {
            fields: uniqFields(obj),
            flatten: true,
            unwind: unwindFields(obj)
        });
};