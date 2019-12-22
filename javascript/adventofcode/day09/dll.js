"use strict";

module.exports = class DLL {

    constructor() {
        this.length = 0;
        this.head = undefined;
        this.tail = undefined;
    }

    print() {
        let contents = "[";

        let node = this.head;
        for (let i = 0; i < this.length; i++) {
            contents += " " + node.value + ",";
            node = node.next;
        }
        contents += " ]";
        console.log(contents);
    };

    insert(value, index) {
        let node = this.emptyNode(value);
        if (this.length === 0) {
            this.head = this.tail = node;
        }
        else if (index === 0) {
            this.insertNewHead(node);
        }
        else if (index === this.length) {
            this.insertNewTail(node);
        }
        else {
            this.insertInteriorNode(node, index);
        }
        this.length += 1;
    };

    insertInteriorNode(node, index) {
        let prev = this.getNode(index - 1);
        let next = prev.next;
        prev.next = node;
        node.prev = prev;
        next.prev = node;
        node.next = next;
    }

    insertNewHead(node) {
        let oldhead = this.head;
        node.next = oldhead;
        oldhead.prev = node;
        this.head = node;
    }

    insertNewTail(node) {
        let oldtail = this.tail;
        oldtail.next = node;
        node.prev = oldtail;
        this.tail = node;
    }

    remove(index) {
        this.length -= 1;
        if (index === 0) {
            return this.removeHead();
        }
        else if (index === this.length) {
            return this.removeTail();
        }
        else {
            return this.removeInterior(index);
        }
    };

    removeInterior(index) {
        let toRemove = this.getNode(index);
        let prev = toRemove.prev;
        let next = toRemove.next;

        prev.next = next;
        next.prev = prev;

        return toRemove.value;
    }

    removeHead() {
        let toRemove = this.head;
        this.head = this.head.next;
        this.head.prev = undefined;
        return toRemove.value;
    }

    removeTail() {
        let toRemove = this.tail;
        this.tail = this.tail.prev;
        this.tail.next = undefined;
        return toRemove.value;
    }

    getNode(index) {
        if (index < (this.length / 2)) {
            let node = this.head;
            for (let i = 0; i < index; i++) {
                node = node.next;
            }
            return node;
        }
        else {
            let node = this.tail;
            for (let i = this.length; i > index; i--) {
                node = node.prev;
            }
            return node;
        }
    }

    emptyNode(value) {
        return {
            value: value,
            prev: undefined,
            next: undefined
        }
    };
};