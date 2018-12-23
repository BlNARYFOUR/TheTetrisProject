"use strict";

document.addEventListener("DOMContentLoaded", init);

const SOUNDS = {
    lineScored: createAudioObj("lineScored.mp3"),
    rotate: createAudioObj("rotate.mp3"),
    hardHit: createAudioObj("hardHit.mp3"),
    gameOver: createAudioObj("gameOver.mp3")
};

function init() {
    window.addEventListener("keydown", keyIn);

    // lineScored();

}

function keyIn(e) {
    console.log("testUp");
    console.log(e.code);
    let key = e.code;

    switch (key) {
        case "ArrowUp":
        case "KeyW":
            SOUNDS.rotate.volume = 0.3;
            SOUNDS.rotate.play();
            break;
        case "Space":
            SOUNDS.hardHit.play();
            break;
    }
}

/*function lineScored() {
    let scored = checkAndScoreFullRow();

    if (scored) {
        SOUNDS.lineScored.play();
    }
}

function checkAndScoreFullRow() {
    let isFullLine = true;
    let totalLineScore = 0;
    let hasScored = false;

    for (let i = 0; i < GAME_BOARD_HEIGHT; i++) {
        totalLineScore = 0;
        isFullLine = true;
        for (let j = 0; j < GAME_BOARD_WIDTH; j++) {
            if (0 < GAME_BOARDS[i][j]) {
                totalLineScore += GAME_BOARDS[i][j];
            } else {
                isFullLine = false;
            }
        }

            if (isFullLine) {
                console.log("You scored a line!");
                hasScored = true;
            }
        }
        return hasScored;
}*/

function createAudioObj(fileName) {
    let audio = document.createElement("audio");
    audio.src = "assets/sounds/" + fileName;
    return audio;
}