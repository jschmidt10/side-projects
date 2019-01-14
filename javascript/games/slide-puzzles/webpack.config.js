const path = require('path');

module.exports = {
  entry: './slide-puzzles.js',
  output: {
    path: path.resolve(__dirname),
    filename: 'slide-puzzles.min.js'
  }
};
