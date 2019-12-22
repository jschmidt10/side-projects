module.exports = class Point {
    constructor(x, y, dx, dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    update() {
        this.x = this.x + this.dx;
        this.y = this.y + this.dy;
    }

    pos() {
        return { x: this.x, y: this.y };
    }

    dist(other) {
        return Math.abs(other.y - this.y) + Math.abs(other.x - this.x);
    }
};