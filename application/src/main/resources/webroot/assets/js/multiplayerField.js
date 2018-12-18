"use strict";

document.addEventListener("DOMContentLoaded", init);

let RESIZE_TIME_INTERVAL;

const BLOCK_NORMAL_RATIO = 32/1920;
const SPACE_NORMAL_RATIO = 15/1920;

let BLOCK = BLOCK_NORMAL_RATIO * document.documentElement.clientWidth;
let SPACE = SPACE_NORMAL_RATIO * document.documentElement.clientWidth;

let GAME_BOARD_SPACING = 4 * BLOCK + 2 * SPACE;

const GAME_BOARD_WIDTH = 10;
const GAME_BOARD_HEIGHT = 18;

let SPACE_TOP = 10 + BLOCK;
let HOLD_WIDTH = 4;
let HOLD_HEIGHT = 4;

let MOCK_PROGRESS = 20;

const IMAGES = {
    backgroundStartScreen: createImageObj("backgroundStartScreen.png"),
    restartPopupPane: createImageObj("restartPopupPane.png"),
    button01: createImageObj("button01.png"),
    restartButton: createImageObj("restartButton.png"),
    retroBackgroundTile: createImageObj("retroBackgroundTile.png"),
    darkBlueTile: createImageObj("darkBlueTile.png"),
    lightBlueTile: createImageObj("lightBlueTile.png"),
    orangeTile: createImageObj("orangeTile.png"),
    redTile: createImageObj("redTile.png"),
    yellowTile: createImageObj("yellowTile.png"),
    greenTile: createImageObj("greenTile.png"),
    purpleTile: createImageObj("purpleTile.png"),
    pikachuTile: createImageObj("pikachu.png"),
    unbreakableTile: createImageObj("unbreakableBlock.png")

};

const COLORS = {
    TRANSPARENT: 0,
    RED: 1,
    DARK_BLUE: 2,
    ORANGE: 3,
    YELLOW: 4,
    LIGHT_BLUE: 5,
    GREEN: 6,
    PURPLE: 7,
    PINK: 8,
    SHITBROWN: 9,
    SHITGREEN: 10
};

let c;
let ctx;

let tiles;
let GAME_BOARDS = [];
let hold;

let CIRCLE = BLOCK * 1.5;
let oldRadius = [0,0,0,0,0];
let progressIntervals = [0,0,0,0,0];

let AMOUNT_OF_PLAYERS = 0;

function init(e) {
    let data = [0];
    // noinspection JSIgnoredPromiseFromCall
    gameCommunication.sendReadyStatus();
    AMOUNT_OF_PLAYERS = localStorage.getItem("amountOfPlayers");

    tiles = new Map();
    tiles.set(COLORS.TRANSPARENT, IMAGES.retroBackgroundTile);
    tiles.set(COLORS.RED, IMAGES.redTile);
    tiles.set(COLORS.DARK_BLUE, IMAGES.darkBlueTile);
    tiles.set(COLORS.ORANGE, IMAGES.orangeTile);
    tiles.set(COLORS.LIGHT_BLUE, IMAGES.lightBlueTile);
    tiles.set(COLORS.YELLOW, IMAGES.yellowTile);
    tiles.set(COLORS.GREEN, IMAGES.greenTile);
    tiles.set(COLORS.PURPLE, IMAGES.purpleTile);
    tiles.set(COLORS.PINK, IMAGES.pinkTile);
    tiles.set(COLORS.SHITBROWN, IMAGES.shitBrownTile);
    tiles.set(COLORS.SHITGREEN, IMAGES.shitGreenTile);
    //tiles.set(COLORS.PIKA, IMAGES.pikachuTile);

    createGameBoard();

    gameLoop();

    window.addEventListener("resize", onResize);
    document.addEventListener("keydown", onKeyDown);
    document.addEventListener("keyup", onKeyUp);
}

function gameLoop() {
    let gameBoardBuf = gameCommunication.getGameBoards();

    //console.log(gameBoardBuf);

    for(let i=0; i<gameBoardBuf.length; i++) {
        GAME_BOARDS[i] = gameBoardBuf[i].slice();
    }

    drawFields();

    window.requestAnimationFrame(gameLoop);
}

function onKeyDown(e) {
    // noinspection JSIgnoredPromiseFromCall
    gameCommunication.sendKey(e.code, true);
}

function onKeyUp(e) {
    // noinspection JSIgnoredPromiseFromCall
    gameCommunication.sendKey(e.code, false);
}

function onResize(e) {
    e.preventDefault();

    clearTimeout(RESIZE_TIME_INTERVAL);
    RESIZE_TIME_INTERVAL = setTimeout(drawFields, 750);
}

function setSizeStuff(playerId) {
    if (playerId === 1) {
        BLOCK = BLOCK_NORMAL_RATIO * document.documentElement.clientWidth;
        SPACE = SPACE_NORMAL_RATIO * document.documentElement.clientWidth;
    } else {
        BLOCK = BLOCK_NORMAL_RATIO * document.documentElement.clientWidth / 2.05;
        SPACE = SPACE_NORMAL_RATIO * document.documentElement.clientWidth / 2.05;
    }

    GAME_BOARD_SPACING = 4 * BLOCK + 2 * SPACE;
    SPACE_TOP = BLOCK * 42/32;
    CIRCLE = BLOCK * 1.5;
}

function drawFields() {
    for (let i = 0; i < AMOUNT_OF_PLAYERS; i++) {
        c = document.getElementById("userField_" + (i+1).toString());
        c.classList.remove("hidden");
        ctx = c.getContext("2d");

        setSizeStuff(i+1);

        c.width = (18 * BLOCK) + (4 * SPACE);
        c.height = 18 * BLOCK;

        drawUserField(ctx, i);
    }
}

function drawUserField(ctx, playerId) {
    ctx.clearRect(0,0, c.width, c.height);

    drawGameBoard(playerId);
    createHold();
    createText(ctx, "Next", SPACE + 1.25 * BLOCK + GAME_BOARD_SPACING + GAME_BOARD_WIDTH * BLOCK, SPACE_TOP - 5, BLOCK);
    createText(ctx, "Hold", SPACE + 1.2 * BLOCK, SPACE_TOP - 5, BLOCK);
    createText(ctx, "Level", SPACE + 1.1 * BLOCK, SPACE_TOP + 12 * BLOCK, BLOCK);
    createText(ctx, "Score", SPACE + 1.25 * BLOCK + GAME_BOARD_SPACING + GAME_BOARD_WIDTH * BLOCK, SPACE_TOP + 12 * BLOCK, BLOCK);
    //createText("3898", SPACE + 1.3 * BLOCK + GAME_BOARD_SPACING + GAME_BOARD_WIDTH * BLOCK, SPACE_TOP + 14.2 * BLOCK);
    createValue(ctx, "5", BLOCK * 2.0, SPACE_TOP + 14.5 * BLOCK, BLOCK);
    drawHold();
    drawNext();
    drawHero();
    drawCircle(ctx, BLOCK * 2.5, SPACE_TOP + 14 * BLOCK,CIRCLE,0,2*Math.PI);
    //drawCircle(SPACE + 2.0 * BLOCK + GAME_BOARD_SPACING + GAME_BOARD_WIDTH * BLOCK, SPACE_TOP + 14 * BLOCK,50,0,2*Math.PI);

    //MOCK_PROGRESS += 20;
    drawProgressBar(playerId, ctx, SPACE + 2.0 * BLOCK + GAME_BOARD_SPACING + GAME_BOARD_WIDTH * BLOCK, SPACE_TOP + 14 * BLOCK, MOCK_PROGRESS, 0, 2*Math.PI, 0.5);
}

function createGameBoard() {
    for(let a = 0; a < AMOUNT_OF_PLAYERS; a++) {
        GAME_BOARDS.push([]);
        for (let i = 0; i < GAME_BOARD_HEIGHT; i++) {
            GAME_BOARDS[a].push(new Array(0));
            for (let j = 0; j < GAME_BOARD_WIDTH; j++) {
                GAME_BOARDS[a][i].push(0);
            }
        }
    }
}

function drawGameBoard(playerID) {
     for (let i = 0; i < GAME_BOARD_HEIGHT; i ++) {
         for (let j = 0; j < GAME_BOARD_WIDTH; j ++) {
           if (GAME_BOARDS[playerID][i][j] !== null) {
               ctx.drawImage(tiles.get(GAME_BOARDS[playerID][i][j]), j * BLOCK + GAME_BOARD_SPACING, i * BLOCK, BLOCK, BLOCK)
           } else {
               ctx.drawImage(IMAGES.unbreakableTile, j * BLOCK + GAME_BOARD_SPACING, i * BLOCK, BLOCK, BLOCK)
           }
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

function drawNext() {
    for (let i = 0; i < HOLD_HEIGHT; i ++) {
        for (let j = 0; j < HOLD_WIDTH; j ++) {
            ctx.drawImage(tiles.get(hold[i][j]), j * BLOCK + SPACE + GAME_BOARD_SPACING + GAME_BOARD_WIDTH * BLOCK, i * BLOCK + SPACE_TOP, BLOCK, BLOCK)
        }
    }
}

function createText(ctx, text, x_co, y_co, block) {
    ctx.globalAlpha = 1;
    ctx.font = 0.8 * block + "px fabian, sans-serif";
    ctx.fillStyle = "#9584FF";
    ctx.fillText(text, x_co, y_co);
}

function createValue(ctx, number, x_co, y_co, block) {
    ctx.font = 2.0 * block + "px fabian, sans-serif";
    ctx.fillStyle = "#9584FF";
    ctx.fillText(number, x_co, y_co);
}

function createImageObj(fileName) {
    let img = new Image();
    img.src = "../assets/media/" + fileName;
    return img;
}

function drawHero() {
    ctx.drawImage(IMAGES.pikachuTile, 1.2 * BLOCK, SPACE_TOP + 6.2 * BLOCK, BLOCK * 3, BLOCK * 3);
}

function drawCircle(ctx, x, y, radius, startAngle, endAngle, counterClockWise) {
    ctx.beginPath();
    ctx.arc(x, y, radius, startAngle, endAngle, counterClockWise);
    ctx.fillStyle = "#9584FF";
    ctx.globalAlpha = 0.35;
    ctx.fill();
    ctx.stroke();
}

function drawProgressBar(id, ctx, x, y, radius, startAngle, endAngle, animationDuration) {
    let playerId = id;

    if(100 < radius) {
        radius = 100;
    }

    let circle = CIRCLE;
    let block = BLOCK;

    radius = circle * radius/100;

    let color = {
        R: 255,
        G: 0,
        B: 0
    };

    if(circle <= radius) {
        color = {
            R: 0,
            G: 255,
            B: 0
        };
    }

    drawCircle(ctx, x, y,circle,0,2*Math.PI);

    ctx.beginPath();
    ctx.arc(x, y, radius, startAngle, endAngle);
    ctx.fillStyle = "rgb(" + color.R + "," + color.G + "," + color.B + ")";
    ctx.globalAlpha = 0.5;
    ctx.fill();
    ctx.globalAlpha = 1;

    createText(ctx, "3898", x-0.7*block, y+0.2*block, block);
}

/*
function drawProgressBar(id, ctx, x, y, radius, startAngle, endAngle, animationDuration) {
    let playerId = id;

    if(100 < radius) {
        radius = 100;
    }

    let circle = CIRCLE;
    let block = BLOCK;

    radius = circle * radius/100;

    let frameRadius = (radius - oldRadius[playerId]) / (animationDuration / 0.01);
    let bufRadius = oldRadius[playerId];

    let color = {
        R: 255,
        G: 0,
        B: 0
    };
    let colorChange = 0;

    if(circle <= radius && oldRadius[playerId] < circle) {
        colorChange += Math.floor(256 / (animationDuration / 0.01));
    } else if(circle <= oldRadius[playerId] && radius < circle) {
        color = {
            R: 0,
            G: 255,
            B: 0
        };
        colorChange -= Math.floor(256 / (animationDuration / 0.01));
    }

    function frame() {
        if (radius - 0.00000001 <= bufRadius && bufRadius <= radius + 0.00000001 ) {
            clearInterval(progressIntervals[playerId]);
            oldRadius[playerId] = radius;
        } else {
            ctx.save();
            ctx.beginPath();
            ctx.arc(x, y, circle, startAngle, endAngle);
            ctx.clip();
            ctx.clearRect(x-circle,y-circle,circle*2,circle*2);
            ctx.restore();

            drawCircle(ctx, x, y,circle,0,2*Math.PI);

            bufRadius += frameRadius;
            ctx.beginPath();
            ctx.arc(x, y, bufRadius, startAngle, endAngle);

            color.R -= colorChange;
            color.G += colorChange;
            ctx.fillStyle = "rgb(" + color.R + "," + color.G + "," + color.B + ")";

            ctx.globalAlpha = 0.5;
            ctx.fill();
            ctx.globalAlpha = 1;
        }

        createText(ctx, "3898", x-0.7*block, y+0.2*block, block);
    }

    progressIntervals[playerId] = setInterval(frame, 10);
}
*/
