"use strict";

import {PATTERNS} from "./blockPatterns.js";

document.addEventListener("DOMContentLoaded", init);

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
const SOUNDS = {
    slowHit: createAudioObj("slowHit.mp3"),
    hardHit: createAudioObj("hardHit.mp3"),
    lineScored: createAudioObj("lineScored.mp3"),
    gameOver: createAudioObj("gameOver.mp3")
};
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
const BUTTONS = {
    playButton: {
        active: false,
        width: 6,
        height: 3,
        xLeft: 0,
        yTop: 0,
        xRight: 0,
        yBottom: 0,
        image: IMAGES.button01,
        action: startTheGame
    },

    restartButton: {
        active: false,
        width: 3,
        height: 3,
        xLeft: 0,
        yTop: 0,
        xRight: 0,
        yBottom: 0,
        image: IMAGES.restartButton,
        action: startTheGame
    }
};
const FONTS = {
    Arcade: "Arcade"
};

const GAME_BOARD_WIDTH = 10;
const GAME_BOARD_HEIGHT = 18;
const BEGIN_MOVEMENT_TIME = 750;
const FAST_MOVEMENT_TIME = 25;

const SUPER_SONIC_POINTS = 5;
const FULL_LINE_POINTS = 10;

let nextBlockFallBusy;

let canvas;
let ctx;
let tiles;

let playingFieldWidth;
let playingFieldHeight;

let blockWidth;
let blockHeight;

let gameBoard;
let canNewGameBegin;
let normalMovementTime;
let currentMovementTime;

let fallingBlock, nextFallingBlock;

let interval;
let goingFast;
let paused;

let score, highscore;

//TODO
let min = document.getElementById("min")  ;
let sec = document.getElementById("sec");
let totalSeconds = 0;
let timer;

function initButtons() {
    BUTTONS.playButton.xLeft = blockWidth * (GAME_BOARD_WIDTH - BUTTONS.playButton.width) / 2;
    BUTTONS.playButton.yTop = blockHeight * (GAME_BOARD_HEIGHT - BUTTONS.playButton.height) / 2;
    BUTTONS.playButton.xRight = BUTTONS.playButton.xLeft + blockWidth * BUTTONS.playButton.width;
    BUTTONS.playButton.yBottom = BUTTONS.playButton.yTop + blockHeight * BUTTONS.playButton.height;

    BUTTONS.restartButton.xLeft = blockWidth * (GAME_BOARD_WIDTH - BUTTONS.restartButton.width) / 2;
    BUTTONS.restartButton.yTop = blockHeight * (GAME_BOARD_HEIGHT - BUTTONS.restartButton.height - 2.5);
    BUTTONS.restartButton.xRight = BUTTONS.restartButton.xLeft + blockWidth * BUTTONS.restartButton.width;
    BUTTONS.restartButton.yBottom = BUTTONS.restartButton.yTop + blockHeight * BUTTONS.restartButton.height;
}

function init() {
    normalMovementTime = BEGIN_MOVEMENT_TIME;
    nextBlockFallBusy = false;
    paused = false;
    goingFast = false;
    score = 0;
    highscore = localStorage.getItem('highscore');

    showScore();

    canvas = document.getElementById("myCanvas");
    ctx = canvas.getContext("2d");

    playingFieldWidth = canvas.width*10/19;
    playingFieldHeight = canvas.height;

     tiles = new Map();
     tiles.set(COLORS.TRANSPARENT, IMAGES.backgroundTile);
     tiles.set(COLORS.RED, IMAGES.redTile);
     tiles.set(COLORS.DARK_BLUE, IMAGES.darkBlueTile);
     tiles.set(COLORS.ORANGE, IMAGES.orangeTile);
     tiles.set(COLORS.LIGHT_BLUE, IMAGES.lightBlueTile);
     tiles.set(COLORS.YELLOW, IMAGES.yellowTile);
     tiles.set(COLORS.GREEN, IMAGES.greenTile);
     tiles.set(COLORS.PURPLE, IMAGES.purpleTile);

    blockWidth = playingFieldWidth / GAME_BOARD_WIDTH;
    blockHeight = playingFieldHeight / GAME_BOARD_HEIGHT;

    fallingBlock = {
        color: 0
    };

    initButtons();
    preLoaderAndDrawBeginScreen();
    canNewGameBegin = true;
    document.addEventListener("click", onClick);
    document.addEventListener("mousemove", onMouseMove);


}

// BRYAN

function setTime() {
    totalSeconds ++;
    sec.innerText = pad(totalSeconds % 60);
    min.innerText = pad(parseInt(totalSeconds / 60));
}

function resetTime() {
    totalSeconds = 0;
    sec.innerText = pad(0);
    min.innerText = pad(0);
}

function pad(val) {
    let valString = val + "";
    if (valString .length < 2){
        return "0" + valString;
    }else {
        return valString;
    }
}

function showScore() {
    document.getElementById("score").innerText = score;
}

function createAudioObj(fileName) {
    let audio = document.createElement("audio");
    audio.src = "assets/sounds/" + fileName;
    return audio;
}

function createImageObj(fileName) {
    let img = new Image();
    img.src = "assets/media/" + fileName;
    return img;
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

function startTheGame() {
    if(canNewGameBegin) {
        score = 0;
        showScore();

        deActivateButton(BUTTONS.playButton);
        deActivateButton(BUTTONS.restartButton);
        timer = setInterval(setTime, 1000);

        createGameBoard();

        getNextFallingBlock();

        fallingBlock = nextFallingBlock;
        getNextFallingBlock();

        console.log(fallingBlock);
        console.log(nextFallingBlock);


        drawGameBoard();
        drawFallingBlock();

        canNewGameBegin = false;

        startInterval(normalMovementTime);

        document.addEventListener("keydown", onKeyDown);
        document.addEventListener("keyup", onKeyUp);
    } else {
        console.error("The game is still running...");
    }
}

function stopTheGame() {
    stopInterval();
    document.removeEventListener("keydown", onKeyDown);
    document.removeEventListener("keyup", onKeyUp);
    SOUNDS.gameOver.play();

    timer = clearInterval(timer);
    timer = resetTime();


    if(highscore < score) {
        highscore = score;
    }

    saveHighscore();

    drawRestartScreen();
    canNewGameBegin = true;
}

function saveHighscore() {
    localStorage.setItem('highscore', highscore);
}

function stopInterval() {
    clearInterval(interval);
}

function startInterval(time) {
    clearInterval(interval);
    interval = setInterval(nextBlockFall, time);
    currentMovementTime = time;
}



function nextBlockFall() {
    nextBlockFallBusy = true;

    let isNew = false;
    let nextY = fallingBlock.y + 1;

    if(checkCollision(fallingBlock.blockPattern, fallingBlock.x, nextY)) {
        isNew = true;
        placeBlock(fallingBlock.blockPattern, fallingBlock.x, fallingBlock.y, fallingBlock.color);

        if(fallingBlock.y <= 0) {
            stopTheGame();
            return isNew;
        } else {
            fallingBlock = nextFallingBlock;
            getNextFallingBlock();
            let scored = checkAndScoreFullRows();

            // animation for scoring is in checkAndScoreFullRows()
            if(scored) {
                SOUNDS.lineScored.play();   //sound for line
            }
            if(currentMovementTime === normalMovementTime) {
                console.log("slowHitSound");
                SOUNDS.slowHit.play();      //play slowHit
            } else {
                console.log("hardHitSound");
                SOUNDS.hardHit.play();      //play hardDHit
            }
        }

    } else {
        fallingBlock.y = nextY;
    }

    updateViewScreen();

    return isNew;
}



function onMouseMove(e) {
    let mouseX = e.pageX - canvas.offsetLeft;
    let mouseY = e.pageY - canvas.offsetTop;
    let isHoveringOverButton = false;

    for(let i=0; i<Object.values(BUTTONS).length; i++) {
        let button = Object.values(BUTTONS)[i];

        if(checkButtonClick(button, mouseX, mouseY) && button.active) {
            isHoveringOverButton = true;
        }
    }

    if(isHoveringOverButton) {
        document.body.style.cursor = "pointer";
    } else {
        document.body.style.cursor = "default";
    }
}

function onClick(e) {
    let mouseX = e.pageX - canvas.offsetLeft;
    let mouseY = e.pageY - canvas.offsetTop;

    console.log("x: " + mouseX + "\ty: " + mouseY);

    for(let i=0; i<Object.values(BUTTONS).length; i++) {
        let button = Object.values(BUTTONS)[i];

        if(checkButtonClick(button, mouseX, mouseY) && button.active) {
            button.action();
        }
    }
}

function onKeyDown(e) {
    e.stopPropagation();

    //if (lastKeyPress >= (Date.now() - delay))
    //    return;
    //lastKeyPress = Date.now();

    removeEventListener("keydown", onKeyDown);

    console.log(e.code);

    switch (e.code) {
        case "KeyW":
        case "ArrowUp":
            tryAndRotate();
            break;
        case "KeyA":
        case "ArrowLeft":
            tryAndGoLeft();
            break;
        case "KeyD":
        case "ArrowRight":
            tryAndGoRight();
            break;
        case "KeyS":
        case "ArrowDown":
            sonic();
            break;
        case "KeyN":
            startTheGame();
            break;
        case "KeyP":
            pauseTheGame();
            break;
        case "Space":
            superSonic();
            break;
        case "KeyG":
            logGameBoard();
            break;
    }
}

function onKeyUp(e) {
    switch (e.code) {
        case "ArrowDown":
        case "KeyS":
            stopSonic();
            break;
    }
}



function checkButtonClick(button, mouseX, mouseY) {
    let isInBounds = false;

    if(button.xLeft <= mouseX && mouseX <= button.xRight && button.yTop <= mouseY && mouseY <= button.yBottom) {
        isInBounds = true;
    }

    return isInBounds;
}

function activateButton(button) {
    button.active = true;
}

function deActivateButton(button) {
    button.active = false;
}



function logGameBoard() {
    console.log(gameBoard);
}



function preLoaderAndDrawBeginScreen() {
    let loaded = Object.values(IMAGES).length;

    for (let i = 0; i < Object.values(IMAGES).length; i++) {
        Object.values(IMAGES)[i].addEventListener("load", function() {
            console.log("loaded Image");
            loaded--;
            if (loaded === 0) {
                drawBeginScreen();
            }
        });
    }
}

function drawBackgroundStartScreen(){
    ctx.drawImage(IMAGES.backgroundStartScreen, 0, 0, playingFieldWidth, playingFieldHeight);
}

function drawPlayGameButton() {
    ctx.drawImage(BUTTONS.playButton.image, BUTTONS.playButton.xLeft, BUTTONS.playButton.yTop, blockWidth * BUTTONS.playButton.width, blockHeight * BUTTONS.playButton.height);

    ctx.font = (BUTTONS.playButton.height-0.5)*blockHeight + "px " + FONTS.Arcade;
    ctx.fillText("Play", BUTTONS.playButton.xLeft + blockWidth*0.85, BUTTONS.playButton.yTop + blockHeight*2.5);

    activateButton(BUTTONS.playButton);
}

function drawRestartStats() {
    ctx.font = 1*blockHeight + "px " + FONTS.Arcade;
    ctx.fillText("Score: " + score, 1.5*blockWidth, 7*blockHeight);
    ctx.fillText("Highscore: " + highscore, 1.5*blockWidth, 8*blockHeight);
}

function drawRestartButton() {
    ctx.drawImage(BUTTONS.restartButton.image, BUTTONS.restartButton.xLeft, BUTTONS.restartButton.yTop, blockWidth * BUTTONS.restartButton.width, blockHeight * BUTTONS.restartButton.height);
    activateButton(BUTTONS.restartButton);

    console.log(BUTTONS.restartButton);
}

function drawRestartPopupPane() {
    ctx.drawImage(IMAGES.restartPopupPane, 0.5*blockWidth, 1.5*blockHeight, playingFieldWidth-1*blockWidth, playingFieldHeight-3*blockHeight);
}

function writeGameOver() {
    ctx.font = 2.5*blockHeight + "px " + FONTS.Arcade;
    ctx.fillText("Game", 2.5*blockWidth, 4*blockHeight);
    ctx.fillText("over", 2.5*blockWidth, 5.25*blockHeight);
}

function drawBeginScreen() {
    drawBackgroundStartScreen();
    drawPlayGameButton();
}

function drawRestartScreen() {
    drawRestartPopupPane();
    writeGameOver();
    drawRestartStats();
    drawRestartButton();
}

function drawScore() {
    ctx.fillText("Play", BUTTONS.playButton.xLeft + blockWidth, BUTTONS.playButton.yTop + blockHeight*2.1);
}

function drawGameBoard() {
    for(let i=0; i<GAME_BOARD_HEIGHT; i++) {
        for(let j=0; j<GAME_BOARD_WIDTH; j++) {
            ctx.drawImage(tiles.get(gameBoard[i][j]), blockWidth*j, blockHeight*i, blockWidth, blockHeight);
        }
    }
}

function drawFallingBlock() {
    let maxHeight = fallingBlock.blockPattern.length;
    let maxWidth = fallingBlock.blockPattern[0].length;

    if(!checkCollision(fallingBlock.blockPattern, fallingBlock.x, fallingBlock.y)) {
        for (let i = 0; i < maxHeight; i++) {
            for (let j = 0; j < maxWidth; j++) {
                if (fallingBlock.blockPattern[i][j] !== COLORS.TRANSPARENT)
                    ctx.drawImage(tiles.get(fallingBlock.color), blockWidth * (fallingBlock.x + j), blockHeight * (fallingBlock.y + i), blockWidth, blockHeight);
            }
        }
    }
}

function updatePlayScreen() {
    drawGameBoard();
    drawFallingBlock();
}

function updateViewScreen() {
    updatePlayScreen();
}



function pauseTheGame() {
    stopInterval();

    if(paused && !canNewGameBegin) {
        startInterval(normalMovementTime);
        console.log("Resumed the game!");
        timer = setInterval(setTime, 1000);
    } else {
        console.log("Paused the game!");
        timer = clearInterval(timer);
    }

    paused = !paused;
}

function tryAndRotate() {
    let tryPattern = rotatePattern(fallingBlock.blockPattern);
    if(!checkCollision(tryPattern, fallingBlock.x, fallingBlock.y)) {
        fallingBlock.blockPattern = tryPattern;
        updatePlayScreen()
    }
}

function tryAndGoLeft() {
    if(!checkCollision(fallingBlock.blockPattern, fallingBlock.x-1, fallingBlock.y)) {
        fallingBlock.x--;
        updatePlayScreen();
    }
}

function tryAndGoRight() {
    if(!checkCollision(fallingBlock.blockPattern, fallingBlock.x+1, fallingBlock.y)) {
        fallingBlock.x++;
        updatePlayScreen();
    }
}

function sonic() {
    if(!goingFast) {
        goingFast = true;
        startInterval(FAST_MOVEMENT_TIME);
    }
}

function superSonic() {
    startInterval(FAST_MOVEMENT_TIME);
    let placed = false;
    let levels = 0;

    do {
        placed = nextBlockFall();
        levels++;
    } while(!placed);

    score += levels * SUPER_SONIC_POINTS;
    showScore();
    startInterval(normalMovementTime);
}

function stopSonic() {
    if(goingFast) {
        goingFast = false;
        startInterval(normalMovementTime);
    }
}



function getNextFallingBlock () {
    nextFallingBlock = {
        blockPattern: PATTERNS[Math.floor(Math.random()*(PATTERNS.length))],
        x: 0,
        y: 0,
        color: Object.values(COLORS)[Math.floor(1 + Math.random()*(Object.values(COLORS).length-1))]
    };

    nextFallingBlock.x = Math.floor((GAME_BOARD_WIDTH - nextFallingBlock.blockPattern[0].length) / 2);
    dealWithNextColor();
   }

function dealWithNextColor() {
    for(let i=0; i<5; i++) {
        nextFallingBlock.color = (nextFallingBlock.color !== fallingBlock.color) ? nextFallingBlock.color : Object.values(COLORS)[Math.floor(1 + Math.random() * (Object.values(COLORS).length - 1))];
    }
}



function emptyLine(lineHeight) {
    for(let j=0; j<GAME_BOARD_WIDTH; j++) {
        gameBoard[lineHeight][j] = 0;
    }
}

function dropTopLayers(lineHeight) {
    console.log("DropTopLayers");
    for(let i=lineHeight; 0<i; i--) {
        gameBoard[i] = gameBoard[i-1].slice();
    }

    emptyLine(0);
}

function checkAndScoreFullRows() {
    let isFullLine = true;
    let totalLineScore = 0;
    let hasScored = false;

    for(let i=0; i<GAME_BOARD_HEIGHT; i++) {
        totalLineScore = 0;
        isFullLine = true;
        for(let j=0; j<GAME_BOARD_WIDTH; j++) {
            if(0 < gameBoard[i][j]) {
                totalLineScore += gameBoard[i][j];
            } else {
                isFullLine = false;
            }
        }
        showScore();

        if(isFullLine) {
            console.log("You scored a line!");
            dropTopLayers(i);
            //animate
            hasScored = true;
            score += totalLineScore * FULL_LINE_POINTS;
            showScore();
        }
    }

    return hasScored;
}



function checkCollision(blockPattern, x, y) {
    let collided = false;
    let maxHeight = blockPattern.length;
    let maxWidth = blockPattern[0].length;

    if(x+maxWidth <= GAME_BOARD_WIDTH && y+maxHeight <= GAME_BOARD_HEIGHT && x >= 0) {
        for(let i=0; i<maxHeight; i++) {
            for (let j = 0; j < maxWidth; j++) {
                if(blockPattern[i][j] === 1 && gameBoard[y+i][x+j] > 0) {
                    collided = true;
                }
            }
        }
    } else {
        collided = true;
    }

    if(collided) {
        console.error("Collision occurred!");
    }

    return collided;
}

function rotatePattern(blockPattern) {
    let maxHeight = blockPattern.length;
    let maxWidth = blockPattern[0].length;
    let rotatedPattern = new Array(0);

    for(let j=0; j<maxWidth; j++) {
        rotatedPattern.push(new Array(0));
        for(let i=maxHeight-1; i>=0; i--) {
            rotatedPattern[j].push(blockPattern[i][j]);
        }
    }

    return rotatedPattern;
}

function placeBlock(blockPattern, x, y, colorIndex) {
    console.log("Try placing block");

    let maxHeight = blockPattern.length;
    let maxWidth = blockPattern[0].length;

    if(!checkCollision(blockPattern, x, y)) {
        for (let i = 0; i < maxHeight; i++) {
            for (let j = 0; j < maxWidth; j++) {
                if (blockPattern[i][j] === 1)
                    gameBoard[y + i][x + j] = colorIndex;
            }
        }

        console.log("Block placed");
    }
}