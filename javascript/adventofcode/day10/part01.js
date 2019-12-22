"use strict";

let fs = require("fs");
let readline = require("readline-sync");
let Point = require("./point");
let Board = require("./board");

let pointRegex = /position=< *(-?[0-9]+), *(-?[0-9]+)> velocity=< *(-?[0-9]+), *(-?[0-9]+)>/;

function createBoard(points) {
    let min = { x: 0, y: 0 };
    let max = { x: 0, y: 0 };

    points.forEach(p => {
        min.x = Math.min(p.x, min.x);
        min.y = Math.min(p.y, min.y);
        max.x = Math.max(p.x, max.x);
        max.y = Math.max(p.y, max.y);
    });

    return new Board(min.x, min.y, max.x, max.y);
}

function readPoint(line) {
    let m = pointRegex.exec(line);
    return new Point(parseInt(m[1]), parseInt(m[2]), parseInt(m[3]), parseInt(m[4]))
}

function hasNeighbors(point, points, numNeighbors, minDistance) {
    var closeNeighbors = 0;

    for (let i = 0; i < points.length; i++) {
        let other = points[i];
        if (other !== point) {
            if (point.dist(other) <= minDistance) {
                closeNeighbors += 1;
                if (closeNeighbors >= numNeighbors) {
                    return true;
                }
            }
        }
    }

    return false;
}

function prune(points, numNeighbors, minDistance) {
    return points.filter(p => hasNeighbors(p, points, numNeighbors, minDistance));
}

let outfile = "index.html";
let infile = process.argv[2];
let lines = fs.readFileSync(infile).toString().split('\n');;
let points = lines.map(readPoint);

var pruned = prune(points, 1, 1);
var board = createBoard(pruned);
var keepRunning = true;

while (keepRunning) {
    let numPruned = points.length - pruned.length;

    console.log(`Board Dims: ${board.width}x${board.height}`);
    console.log(`Pruned ${numPruned}/${points.length} `)

    if (numPruned === 0 || board.width < 1000) {
        console.log("No points pruned, printing");
        board.print(pruned, outfile);
        if (!readline.keyInYN("Continue?")) {
            keepRunning = false;
        }
    }
    else {
        console.log("Points pruned, continuing");
    }

    points.forEach(p => p.update());
    pruned = prune(points, 1, 1);
    board = createBoard(pruned);
}