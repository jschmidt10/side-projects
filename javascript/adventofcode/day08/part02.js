let fs = require("fs");

let parser = {
  line: '',
  offset: 0,
  tree: null,

  parse: function() {
    this.tree = this.readTree();
  },

  readTree: function() {
    let numChildNodes = this.nextInt();
    let numMetadataNodes = this.nextInt();

    let children = [];
    for (let i = 0; i < numChildNodes; i++) {
      children.push(this.readTree());
    }

    let metadata = [];
    for (let i = 0; i < numMetadataNodes; i++) {
      let m = this.nextInt();
      metadata.push(m);
    }

    return this.newTree(children, metadata);
  },

  newTree: function(children, metadata) {
    return {
      children,
      metadata
    };
  },

  nextInt: function() {
    let str = "";
    while (this.line[this.offset] !== ' ' && this.line[this.offset] !== undefined) {
      str += this.line[this.offset++];
    }
    this.offset++; // progress past space
    return parseInt(str);
  },

  getTreeVal: function(tree = this.tree) {
    if (tree.val !== undefined) {
      return tree.val;
    } else if (tree.children.length === 0) {
      let val = tree.metadata.reduce((x, y) => x + y);
      tree.val = val;
      return val;
    } else {
      let val = 0;
      for (let i = 0; i < tree.metadata.length; i++) {
        let child = tree.children[tree.metadata[i] - 1];
        if (child !== undefined) {
          val += this.getTreeVal(child);
        }
      }
      tree.val = val;
      return val;
    }
  }
};

let filename = process.argv[2];
parser.line = fs.readFileSync(filename).toString();
parser.parse();

console.log(parser.getTreeVal());
