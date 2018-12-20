let fs = require("fs");

let parser = {
    line: '',
    offset: 0,
    metadata: [],

    readTree: function () {
        let numChildNodes = this.nextInt();
        let numMetadataNodes = this.nextInt();

        for (let i = 0; i < numChildNodes; i++) {
            this.readTree();
        }

        for (let i = 0; i < numMetadataNodes; i++) {
            let m = this.nextInt();
            this.metadata.push(m);
        }
    },

    nextInt: function () {
        let str = "";
        while (this.line[this.offset] !== ' ' && this.line[this.offset] !== undefined) {
            str += this.line[this.offset++];
        }
        this.offset++; // progress past space
        return parseInt(str);
    }
};

let filename = process.argv[2];
parser.line = fs.readFileSync(filename).toString();
parser.readTree();

console.log(parser.metadata.reduce((x, y) => x + y));