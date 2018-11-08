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
    gameOver: createAudioObj("gameOver.mp3"),
    music: createAudioObj("music.mp3"),
    startGame: createAudioObj("start.mp3"),
    pause: createAudioObj("pause.mp3"),
    rotate: createAudioObj("rotate.mp3"),
    select: createAudioObj("select.mp3")
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
const BUTTONS = {};

const FONTS = {
    Arcade: "Arcade"
};

const KEY_DOWN_FUNC = {
    "KeyW": tryAndRotate,
    "ArrowUp": tryAndRotate,
    "KeyA": tryAndGoLeft,
    "ArrowLeft": tryAndGoLeft,
    "KeyD": tryAndGoRight,
    "ArrowRight": tryAndGoRight,
    "KeyS": sonic,
    "ArrowDown": sonic,
    "KeyN": startTheGame,
    "KeyP": pauseTheGame,
    "Space": superSonic,
    "KeyG": logGameBoard,
    // TODO KeyM werkt nog niet
    /*"KeyM": soundSettings*/
};

const GAME_BOARD_WIDTH = 10;
const GAME_BOARD_HEIGHT = 18;

const NEXT_BOARD_WIDTH = 7;
const NEXT_BOARD_HEIGHT = 3.5;

const BEGIN_MOVEMENT_TIME = 750;
const FAST_MOVEMENT_TIME = 25;

const SUPER_SONIC_POINTS = 5;
const FULL_LINE_POINTS = 10;

let nextBlockFallBusy;

let canvas;
let ctx;
let tiles;

// TODO TEST
let canvasNext;
let ctxNext;
let tiles2;
let playingFieldWidth2;
let playingFieldHeight2;
let blockWidth2;
let blockHeight2;

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
let amountOfScoredLines, level;

// Bryan added
let hour = document.getElementById("hour");
let min = document.getElementById("min")  ;
let sec = document.getElementById("sec");
let totalSeconds = 0;
let timer;


function Button(width, height, xLeft, yTop, xRight, yBottom, image, action) {
    this.active = false;
    this.width = width;
    this.height = height;
    this.xLeft = xLeft;
    this.yTop = yTop;
    this.xRight = xRight;
    this.yBottom = yBottom;
    this.image = image;
    this.action = action;
}

function initButtons() {
    let name;
    let width;
    let height;
    let xLeft;
    let yTop;
    let xRight;
    let yBottom;
    let image;
    let action;

    name = "playButton";
    width = 6;
    height = 3;
    xLeft = blockWidth * (GAME_BOARD_WIDTH - width) / 2;
    yTop = blockHeight * (GAME_BOARD_HEIGHT - height) / 2;
    xRight = xLeft + blockWidth * width;
    yBottom = yTop + blockHeight * height;
    image = IMAGES.button01;
    action = startTheGame;

    BUTTONS[name] = new Button(width, height, xLeft, yTop, xRight, yBottom, image, action);

    name = "restartButton";
    width = 3;
    height = 3;
    xLeft = blockWidth * (GAME_BOARD_WIDTH - width) / 2;
    yTop = blockHeight * (GAME_BOARD_HEIGHT - height - 2.5);
    xRight = xLeft + blockWidth * width;
    yBottom = yTop + blockHeight * height;
    image = IMAGES.restartButton;
    action = startTheGame;

    BUTTONS[name] = new Button(width, height, xLeft, yTop, xRight, yBottom, image, action);
}

function init() {
    normalMovementTime = BEGIN_MOVEMENT_TIME;
    nextBlockFallBusy = false;
    paused = false;
    goingFast = false;
    score = 0;
    amountOfScoredLines = 0;
    level = 1;
    highscore = localStorage.getItem('highscore');

    // Bryan added
    showScore();

    document.getElementById("speaker").addEventListener("click", soundSettings);
    document.getElementById("pause").addEventListener("click", pauseTheGame);

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

     SOUNDS.rotate.volume = 0.3;

    blockWidth = playingFieldWidth / GAME_BOARD_WIDTH;
    blockHeight = playingFieldHeight / GAME_BOARD_HEIGHT;
    console.log(blockWidth);

    //TODO test
    canvasNext = document.getElementById("next");
    ctxNext = canvasNext.getContext("2d");
    ctxNext.fillText("NEXT BLOCK", 5, 10);

    playingFieldWidth2 = canvasNext.width;
    playingFieldHeight2 = canvasNext.height;

    tiles2 = new Map();
    tiles2.set(COLORS.TRANSPARENT, IMAGES.backgroundTile);
    tiles2.set(COLORS.RED, IMAGES.redTile);
    tiles2.set(COLORS.DARK_BLUE, IMAGES.darkBlueTile);
    tiles2.set(COLORS.ORANGE, IMAGES.orangeTile);
    tiles2.set(COLORS.LIGHT_BLUE, IMAGES.lightBlueTile);
    tiles2.set(COLORS.YELLOW, IMAGES.yellowTile);
    tiles2.set(COLORS.GREEN, IMAGES.greenTile);
    tiles2.set(COLORS.PURPLE, IMAGES.purpleTile);

    blockWidth2 = playingFieldWidth2 / NEXT_BOARD_WIDTH;
    blockHeight2 = playingFieldHeight2 / NEXT_BOARD_HEIGHT;

    fallingBlock = {
        color: 0
    };

    initButtons();
    preLoaderAndDrawBeginScreen();
    canNewGameBegin = true;
    document.addEventListener("click", onClick);
    document.addEventListener("mousemove", onMouseMove);
}

// Bryan added
function soundSettings() {
    if (document.getElementById("speaker").className === "soundON"){
        SOUNDS.music.muted = true;
        SOUNDS.rotate.muted = true;
        SOUNDS.hardHit.muted = true;
        SOUNDS.slowHit.muted = true;
        SOUNDS.select.muted = true;
        SOUNDS.startGame.muted = true;
        SOUNDS.gameOver.muted = true;
        SOUNDS.pause.muted = true;
        SOUNDS.lineScored.muted = true;
        document.getElementById("speaker").className = "soundOFF";
        document.getElementById("speaker").innerHTML = '<i class="material-icons">volume_off</i>';

        console.log("set sound off")
    }else {
        SOUNDS.music.muted = false;
        SOUNDS.rotate.muted = false;
        SOUNDS.hardHit.muted = false;
        SOUNDS.slowHit.muted = false;
        SOUNDS.select.muted = false;
        SOUNDS.startGame.muted = false;
        SOUNDS.gameOver.muted = false;
        SOUNDS.pause.muted = false;
        SOUNDS.lineScored.muted = false;

        document.getElementById("speaker").className = "soundON";
        document.getElementById("speaker").innerHTML = '<i class="material-icons">volume_up</i>';
        console.log("set sound on")
    }
}



function setTime() {
    totalSeconds ++;
    sec.innerText = pad(totalSeconds % 60);
    min.innerText = pad(parseInt(totalSeconds / 60));
    hour.innerText = pad(parseInt((totalSeconds / 60) / 60));
}

function resetTime() {
    totalSeconds = 0;
    sec.innerText = pad(0);
    min.innerText = pad(0);
    hour.innerText = pad(0)
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
    document.getElementById("points").innerText = score;
}

function createAudioObj(fileName) {
    let audio = document.createElement("audio");
    audio.src = "../assets/sounds/" + fileName;
    return audio;
}

function createImageObj(fileName) {
    let img = new Image();
    img.src = "../assets/media/" + fileName;
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
        //Bryan added
        showScore();

        deActivateButton(BUTTONS.playButton);
        deActivateButton(BUTTONS.restartButton);

        //Bryan added
        timer = setInterval(setTime, 1000);

        createGameBoard();

        getNextFallingBlock();
        fallingBlock = nextFallingBlock;
        getNextFallingBlock();

        console.log(fallingBlock);
        console.log(nextFallingBlock);

        drawGameBoard();
        drawFallingBlock();
        drawNextFallingBlock();

        canNewGameBegin = false;

        normalMovementTime = BEGIN_MOVEMENT_TIME;
        startInterval(normalMovementTime);

        document.addEventListener("keydown", onKeyDown);
        document.addEventListener("keyup", onKeyUp);

        startMusic();
    } else {
        console.error("The game is still running...");
    }
}

function startMusic() {
    SOUNDS.startGame.play().then(function () {
        SOUNDS.music.loop = true;
        SOUNDS.music.volume = 0.2;
        SOUNDS.music.currentTime = 0;
        SOUNDS.music.play();
    })
}

function stopTheGame() {
    stopInterval();
    document.removeEventListener("keydown", onKeyDown);
    document.removeEventListener("keyup", onKeyUp);
    //Bryan added
    timer = clearInterval(timer);
    timer = resetTime();

    if(highscore < score) {
        highscore = score;
    }

    saveHighscore();

    endMusic();

    drawRestartScreen();
    canNewGameBegin = true;
}

function stopMainMusicAndPlay(sound) {
    let fadeAudio = setInterval(function () {
        console.log(SOUNDS.music.volume);

        if (SOUNDS.music.volume >= 0.1) {
            SOUNDS.music.volume -= 0.1;
        } else {
            clearInterval(fadeAudio);
            sound.play();
        }
    }, 100);
}

function endMusic() {
    stopMainMusicAndPlay(SOUNDS.gameOver);
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
                SOUNDS.lineScored.currentTime = 0;
                SOUNDS.lineScored.play();   //sound for line
            }
            if(currentMovementTime === normalMovementTime) {
                console.log("slowHitSound");
                SOUNDS.slowHit.currentTime = 0;
                SOUNDS.slowHit.play();      //play slowHit
            } else {
                console.log("hardHitSound");
                SOUNDS.hardHit.currentTime = 0;
                SOUNDS.hardHit.play();      //play hardDHit
            }

            if(10*level <= amountOfScoredLines) {
                level++;
                console.log("LEVEL UP: " + level);
                normalMovementTime /= 1.5;
                console.log("SPEED: " + normalMovementTime);
                startInterval(normalMovementTime);
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

        if(checkButtonBounds(button, mouseX, mouseY) && button.active) {
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

        if(checkButtonBounds(button, mouseX, mouseY) && button.active) {
            SOUNDS.select.play().then(function () {
                setTimeout(button.action, 350);
            });
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

    if(!paused) {
        KEY_DOWN_FUNC[e.code]();
    } else {
        if(e.code === "KeyP") {
            pauseTheGame();
        }
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



function checkButtonBounds(button, mouseX, mouseY) {
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
    ctxNext.drawImage(IMAGES.backgroundStartScreen, 0, 0, playingFieldWidth2, playingFieldHeight2);
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

function drawNextFallingBlock() {
    ctxNext.clearRect(0, 0, canvasNext.width, canvasNext.height);
    ctxNext.fillText("NEXT BLOCK", 5, 10);
    let maxHeight = nextFallingBlock.blockPattern.length;
    let maxWidth = nextFallingBlock.blockPattern[0].length;

    if (!checkCollision(nextFallingBlock.blockPattern, nextFallingBlock.x, nextFallingBlock.y)) {
        for (let i = 0; i < maxHeight; i++) {
            for (let j = 0; j < maxWidth; j++) {
                if (nextFallingBlock.blockPattern[i][j] !== COLORS.TRANSPARENT)
                    ctxNext.drawImage(tiles2.get(nextFallingBlock.color), blockWidth2 * (nextFallingBlock.x + j), blockHeight2 * (nextFallingBlock.y + i), blockWidth2, blockHeight2);

            }
        }
    }
}



function updatePlayScreen() {
    drawGameBoard();
    drawFallingBlock();
    drawNextFallingBlock();
}

function updateViewScreen() {
    updatePlayScreen();
}



function pauseTheGame() {
    stopInterval();

    if(paused && !canNewGameBegin) {
        startInterval(normalMovementTime);
        startMusic();
        console.log("Resumed the game!");
        //Bryan added
        timer = setInterval(setTime, 1000);
    } else {
        console.log("Paused the game!");
        //Bryan added
        timer = clearInterval(timer);
        stopMainMusicAndPlay(SOUNDS.pause);
    }

    paused = !paused;
}

function tryAndRotate() {
    let tryPattern = rotatePattern(fallingBlock.blockPattern);
    if(!checkCollision(tryPattern, fallingBlock.x, fallingBlock.y)) {
        fallingBlock.blockPattern = tryPattern;
        updatePlayScreen();

        SOUNDS.rotate.currentTime = 0;
        SOUNDS.rotate.play();
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
    //Bryan added
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
    gameBoard[lineHeight] = new Array(GAME_BOARD_WIDTH);

    for(let j=0; j<GAME_BOARD_WIDTH; j++) {
        gameBoard[lineHeight][j] = 0;
    }
}

function dropTopLayers(lineHeight) {
    console.log("DropTopLayers");
    for(let i=lineHeight; 0<i; i--) {
        gameBoard[i] = gameBoard[i-1]; //.slice();
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

        if(isFullLine) {
            console.log("You scored a line!");
            dropTopLayers(i);
            //animate
            hasScored = true;
            score += totalLineScore * FULL_LINE_POINTS;
            //Bryan added
            showScore();
            amountOfScoredLines++;
            console.log(amountOfScoredLines);
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
