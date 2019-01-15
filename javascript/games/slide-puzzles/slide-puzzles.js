let conf = {
    rows: 4,
    cols: 4,
    canvas: {
        width: 400,
        height: 400
    },
    blocks: {
        color: "#3f0202",
        trim: {
            color: "white",
            offsetPercent: 0.05,
            lineWidth: 2
        },
        label: {
            color: "white",
            heightOffsetPerfect: 0.75,
            smallWidthOffsetPercent: 0.3,
            largeWidthOffsetPercent: 0.08,
            font: "72px Courier New"
        }
    }
};

// Utility for drawing the puzzle
function PuzzleRenderer(canvasId, conf, forceNew) {

    let puzzle = new Puzzle(conf.rows, conf.cols, forceNew);
    let canvas = document.getElementById(canvasId);
    let ctx = canvas.getContext("2d");

    resize(canvas);
    deriveBlockConfs(conf);

    canvas.addEventListener("click", function (event) {
        let { x, y } = toCanvasCoords(event);
        let row = Math.floor(y / conf.blocks.height);
        let col = Math.floor(x / conf.blocks.width);

        let index = getBlockIndex(row, col);

        puzzle.slide(index);

        drawPuzzle();
    });

    // Maps a row and column to a block index
    function getBlockIndex(row, col) {
        return col + row * conf.cols;
    }

    // Translates a click event from page coords to canvas coords
    function toCanvasCoords(event) {
        return {
            x: event.pageX - canvas.offsetLeft,
            y: event.pageY - canvas.offsetTop
        }
    }

    // Draws the puzzle
    function drawPuzzle() {
        // Clear the previous drawing
        ctx.clearRect(0, 0, conf.canvas.width, conf.canvas.height);

        for (let i = 0; i < puzzle.blocks.length; i++) {
            let block = puzzle.blocks[i];
            if (block !== null) {
                drawBlock(block, i);
            }
        }
    }

    // Draws a block at the specified index
    function drawBlock(block, index) {
        let { x: row, y: col } = puzzle.getCoord(index);
        let x = row * conf.blocks.height;
        let y = col * conf.blocks.width;

        // draw block
        ctx.fillStyle = conf.blocks.color;
        ctx.fillRect(x, y, conf.blocks.width, conf.blocks.height);

        // add trim
        ctx.strokeStyle = conf.blocks.trim.color;
        ctx.lineWidth = conf.blocks.trim.lineWidth;
        ctx.strokeRect(x + conf.blocks.trim.dx, y + conf.blocks.trim.dy, conf.blocks.trim.width, conf.blocks.trim.height);

        // add label
        ctx.fillStyle = conf.blocks.label.color;
        ctx.font = conf.blocks.label.font;
        ctx.fillText(block, x + conf.blocks.label.getDx(block), y + conf.blocks.label.dy);
    }

    // Derives computed configs
    function deriveBlockConfs(conf) {
        conf.blocks.width = conf.canvas.width / conf.cols;
        conf.blocks.height = conf.canvas.height / conf.rows;

        conf.blocks.trim.width = conf.blocks.width * (1 - (conf.blocks.trim.offsetPercent * 2));
        conf.blocks.trim.height = conf.blocks.height * (1 - (conf.blocks.trim.offsetPercent * 2));
        conf.blocks.trim.dx = conf.blocks.width * conf.blocks.trim.offsetPercent;
        conf.blocks.trim.dy = conf.blocks.height * conf.blocks.trim.offsetPercent;

        conf.blocks.label.dy = conf.blocks.height * conf.blocks.label.heightOffsetPerfect;
        conf.blocks.label.getDx = function (block) {
            if (block < 10) {
                return conf.blocks.width * conf.blocks.label.smallWidthOffsetPercent;
            }
            else {
                return conf.blocks.width * conf.blocks.label.largeWidthOffsetPercent;
            }
        };
    }

    // Resize the canvas according to the conf
    function resize(canvas) {
        canvas.width = conf.canvas.width;
        canvas.height = conf.canvas.height;
    }

    drawPuzzle();
}

// The slide puzzle!
function Puzzle(rows, cols, forceNew) {

    this.rows = rows;
    this.cols = cols;

    this.blocks = getBlocks(rows * cols, forceNew);
    this.emptyIndex = this.blocks.indexOf(null);

    // Checks if you won!
    this.isComplete = function () {
        if (this.emptyIndex !== this.blocks.length - 1) {
            return false;
        }
        else {
            for (let i = 0; i < this.blocks.length - 1; i++) {
                if (i !== this.blocks[i]) {
                    return false;
                }
            }
            return true;
        }
    }

    // Prints the blocks for debugging
    this.print = function () {
        for (let i = 0; i < this.blocks.length; i++) {
            console.log(`${i}: ${this.blocks[i]}`);
        }
    }

    // Attempts to slide a block if possible (it must be adjacent to the empty spot)
    this.slide = function (blockIndex) {
        if (isAdjacent(this, blockIndex, this.emptyIndex)) {
            swap(this.blocks, blockIndex, this.emptyIndex);
            this.emptyIndex = blockIndex;
            saveState(this.blocks);
        }
    }

    // Swaps two blocks
    function swap(blocks, index1, index2) {
        let t = blocks[index1];
        blocks[index1] = blocks[index2];
        blocks[index2] = t;
    }

    // Checks if two blocks are adjacent
    function isAdjacent(puzzle, index1, index2) {
        let { x: x1, y: y1 } = puzzle.getCoord(index1);
        let { x: x2, y: y2 } = puzzle.getCoord(index2);

        return (Math.abs(x1 - x2) + Math.abs(y1 - y2)) <= 1;
    }

    // Converts ad index into x, y coords
    this.getCoord = function (index) {
        let x = index % this.cols;
        let y = Math.floor(index / this.cols);
        return { x, y };
    }

    // Attempts to save the blocks in local storage
    function saveState(blocks) {
        if (typeof(Storage) !== undefined) {
            localStorage.setItem("blocks", JSON.stringify(blocks));
        }
    }

    // Attempts to fetch the previous game state if local storage is available and 'forceNew' is false.
    // Otherwise, creates a fresh game state.
    function getBlocks(numBlocks, forceNew) {
        if (typeof (Storage) !== undefined && !forceNew) {
            try {
                let blocks = JSON.parse(localStorage.getItem("blocks"));
                if (validateState(blocks, numBlocks)) {
                    return blocks;
                }    
            }
            catch (err) {
                localStorage.removeItem("blocks");
            }
        }
        
        let blocks = shuffleBlocks(initGame(numBlocks));
        saveState(blocks);
        return blocks;
    }

    // Validate the blocks that were previously saved. Ensures the correct number of blocks is present 
    // and only one empty slot exists.
    function validateState(blocks, numBlocks) {
        return blocks !== null &&
            blocks.length === numBlocks && 
            blocks.filter(b => b === null).length === 1;
    }

    // Shuffles the blocks
    function shuffleBlocks(blocks) {
        for (let i = 0; i < blocks.length; i++) {
            let swapIndex = Math.floor(Math.random() * blocks.length);
            if (swapIndex !== i) {
                swap(blocks, i, swapIndex);
            }
        }

        return blocks;
    }

    // Initializes the slide blocks with the last slot empty
    function initGame(size) {
        let blocks = new Array(size);
        for (let i = 0; i < size - 1; i++) {
            blocks[i] = i;
        }
        blocks[size - 1] = null;
        return blocks;
    }
}

function newPuzzle(forceNew) {
    new PuzzleRenderer("game-canvas", conf, forceNew);
}

function start() {
    document.getElementById("new-puzzle").addEventListener("click", e => newPuzzle(true));
    newPuzzle(false);
}

document.addEventListener("DOMContentLoaded", start);