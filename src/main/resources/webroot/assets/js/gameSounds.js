"use strict";

document.addEventListener("DOMContentLoaded", init);

const SOUNDS = {
    lineScored: createAudioObj("lineScored.mp3"),
    rotate: createAudioObj("rotate.mp3")
};

/*const KEY_DOWN_FUNC = {
    "KeyW": tryAndRotate,
    "ArrowUp": tryAndRotate,
};*/

function init() {
    window.addEventListener("keydown", keyIn);

}

function tryAndRotate() {
    SOUNDS.rotate.volume = 0.3;
    SOUNDS.rotate.play();
}

function keyIn(e) {
    console.log("testUp");
    console.log(e.code);
    let key = e.code;
    if (key === "ArrowUp" || key === "KeyW") {
        tryAndRotate();
    }
}

function createAudioObj(fileName) {
    let audio = document.createElement("audio");
    audio.src = "/static/assets/sounds/" + fileName;
    return audio;
}