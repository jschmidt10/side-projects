"use strict";

const fs = require("fs");
const convert = require("./convert");

/*
 * Converts a JSON file into a CSV file with the same name.
 */
function convertFile(inputDir, inputFile, outputDir) {
    let data = fs.readFileSync(inputDir + "/" + inputFile);
    let outputFile = outputDir + "/" + inputFile.replace(/.json$/, ".csv");
    fs.writeFileSync(outputFile, convert(data));
    return outputFile;
}

/*
 * Converts all of the JSON files in the inputDir and puts the results in the outputDir.
 */
function convertDir(inputDir, outputDir) {
    return new Promise(function (resolve, reject) {
        let jsonFiles = fs.readdirSync(inputDir).filter(f => f.endsWith(".json"));

        if (jsonFiles.length === 0) {
            reject("Found no json files in " + inputDir);
        }
        else {
            let results = [];
            for (let i = 0; i < jsonFiles.length; i++) {
                try {
                    results.push(convertFile(inputDir, jsonFiles[i], outputDir));
                }
                catch (err) {
                    reject(err);
                }
            }
            resolve(results);
        }
    });
}

module.exports = convertDir;