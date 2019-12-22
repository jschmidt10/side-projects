let fs = require("fs");

module.exports = class Board {
    constructor(minX, minY, maxX, maxY) {
        this.startX = minX;
        this.startY = minY;
        this.width = maxX - minX + 1;
        this.height = maxY - minY + 1;
    }

    print(points, filename) {
        if (fs.existsSync(filename)) {
            fs.unlinkSync(filename);
        }

        let startPage = "<html><head><title>Day10</title></head><body><pre>\n";

        fs.writeFileSync(filename, startPage, {flag: 'a'});

        for (let i = 0; i < this.height; i++) {
            
            var line = "";
            for (let j = 0; j < this.width; j++) {
                let px = this.startX + j;
                let py = this.startY + i;
                let p = points.find(p => p.x === px && p.y === py);
                if (p) {
                    line += "# ";
                }
                else {
                    line += ". ";
                }
            }
            line += '\n';

            fs.writeFileSync(filename, line, {flag: 'a'});
        }

        let endPage = "</pre></body></html>";
        fs.writeFileSync(filename, endPage, {flag: 'a'});
    }
};