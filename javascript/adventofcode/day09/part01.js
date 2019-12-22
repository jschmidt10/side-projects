"use strict";

function makeGame(numMarbles, numPlayers) {
    let game = {
        marbles: [],
        players: [],
        currentMarble: undefined,
        currentPlayer: 0,
        numMarbles,
        numPlayers        
    };

    for (let i = 0; i < numPlayers; i++) {
        game.players.push(0);
    }

    return game;
}

function play(game) {
    game.marbles.push(0);
    for (let i = 1; i <= game.numMarbles; i++) {
        if (i % 100000 === 0) {
            console.log(`Placing marble ${i}`);
        }
        if (i % 23 === 0) {
            scoreMarble(game, i);
        }
        else {
            placeMarble(game, i);
        }
        game.currentPlayer = (game.currentPlayer + 1) % game.players.length;
    }
}

function scoreMarble(game, marble) {
    let index = getRemoveIndex(game);
    let [ removed ] = game.marbles.splice(index, 1);
    scoreCurrentPlayer(game, marble + removed);
    game.currentMarble = getCW(index, 1, game.marbles.length);
}

function scoreCurrentPlayer(game, score) {
    game.players[game.currentPlayer] += score;
}

function getRemoveIndex(game) {
  return getCCW(game.currentMarble, 7, game.marbles.length);
}

function placeMarble(game, marble) {
  let index = getInsertIndex(game);
  game.marbles.splice(index, 0, marble);
  game.currentMarble = index;
}

function getInsertIndex(game) {
    if (game.currentMarble === undefined) {
        return 0;
    }
    else {
        return getCW(game.currentMarble, 1, game.marbles.length);
    }
}

function getCW(index, offset, arrlen) {
    let cw = (index - offset) % arrlen;
    if (cw < 0) {
        cw += arrlen;
    }
    return cw;
}

function getCCW(index, offset, arrlen) {
    return (index + offset) % arrlen;
}

let game = makeGame(7151000, 447);
play(game);

console.log(game.players.reduce((x, y) => Math.max(x, y)));
