"use strict";

document.addEventListener("DOMContentLoaded", init);

const BLOCK = 32;
const SPACE = 15;
const GAME_BOARD_SPACING = 4 * BLOCK + 2 * SPACE;

const GAME_BOARD_WIDTH = 10;
const GAME_BOARD_HEIGHT = 18;

const SPACE_TOP = 10 + BLOCK;
const SPACE_BETWEEN = 86;
const HOLD_WIDTH = 4;
const HOLD_HEIGHT = 4;

const IMAGES = {
    backgroundStartScreen: createImageObj("backgroundStartScreen.png"),
    restartPopupPane: createImageObj("restartPopupPane.png"),
    button01: createImageObj("button01.png"),
    restartButton: createImageObj("restartButton.png"),
    backgroundTile: createImageObj("backgroundTile.png"),
    darkBlueTile: createImageObj("darkBlueTile.png"),
    lightBlueTile: createImageObj("lightBlueTile.png"),
    orangeTile: createImageObj("orangeTile.png"),
    redTile: createImageObj("redTile.png"),
    yellowTile: createImageObj("yellowTile.png"),
    greenTile: createImageObj("greenTile.png"),
    purpleTile: createImageObj("purpleTile.png")
};

const COLORS = {
    TRANSPARENT: 0,
    RED: 1,
    DARK_BLUE: 2,
    ORANGE: 3,
    YELLOW: 4,
    LIGHT_BLUE: 5,
    GREEN: 6,
    PURPLE: 7
};

let c;
let ctx;

let tiles;
let gameBoard;
let hold;

function init(e) {
    c = document.getElementById("userField");
    ctx = c.getContext("2d");
    c.width = (18 * BLOCK) + (4 * SPACE);
    c.height = 18 * BLOCK;

    tiles = new Map();
    tiles.set(COLORS.TRANSPARENT, IMAGES.backgroundTile);
    tiles.set(COLORS.RED, IMAGES.redTile);
    tiles.set(COLORS.DARK_BLUE, IMAGES.darkBlueTile);
    tiles.set(COLORS.ORANGE, IMAGES.orangeTile);
    tiles.set(COLORS.LIGHT_BLUE, IMAGES.lightBlueTile);
    tiles.set(COLORS.YELLOW, IMAGES.yellowTile);
    tiles.set(COLORS.GREEN, IMAGES.greenTile);
    tiles.set(COLORS.PURPLE, IMAGES.purpleTile);

    createGameBoard();
    createHold();
    createText();
    drawUserField();
}

function drawUserField() {

    drawGameBoard();
    drawHold();
    
}

function createGameBoard() {
    gameBoard = new Array(0);

    for(let i=0; i<GAME_BOARD_HEIGHT; i++) {
        gameBoard.push(new Array(0));
        for(let j=0; j<GAME_BOARD_WIDTH; j++) {
            gameBoard[i].push(0);
        }
    }
}

function drawGameBoard() {
     for (let i = 0; i < GAME_BOARD_HEIGHT; i ++) {
         for (let j = 0; j < GAME_BOARD_WIDTH; j ++) {
            ctx.drawImage(tiles.get(gameBoard[i][j]), j * BLOCK + GAME_BOARD_SPACING, i * BLOCK, BLOCK, BLOCK)
         }
     }
}

function createHold() {
    hold = new Array(0);

    for(let i=0; i<HOLD_HEIGHT; i++) {
        hold.push(new Array(0));
        for(let j=0; j<HOLD_WIDTH; j++) {
            hold[i].push(0);
        }
    }
}

function drawHold() {
    for (let i = 0; i < HOLD_HEIGHT; i ++) {
        for (let j = 0; j < HOLD_WIDTH; j ++) {
            ctx.drawImage(tiles.get(hold[i][j]), j * BLOCK + SPACE, i * BLOCK + SPACE_TOP, BLOCK, BLOCK)
        }
    }
}

function createText() {
    ctx.font = 0.8 * BLOCK + "px fabian, sans-serif";
    ctx.fillStyle = "#9584FF";
    ctx.fillText("Hold", SPACE + BLOCK, SPACE_TOP - 5);
}

function createImageObj(fileName) {
    let img = new Image();
    img.src = "../assets/media/" + fileName;
    return img;
}

