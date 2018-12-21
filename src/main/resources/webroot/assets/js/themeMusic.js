"use strict";

document.addEventListener("DOMContentLoaded", init);

const sounds = createAudioObj("themeMusic.mp3");

function init() {
    startMusic();
    document.getElementById("toggleMusic").addEventListener("click", toggleMusic);
}

function startMusic() {
        sounds.loop = true;
        sounds.volume = 0.2;
        sounds.currentTime = 0;
        sounds.play();
}

function toggleMusic() {
    if (document.getElementById("toggleMusic").className === "soundON"){
        sounds.muted = true;
        document.getElementById("toggleMusic").className = "soundOFF";
        console.log("turned OFF");
    } else {
        sounds.muted = false;
        document.getElementById("toggleMusic").className = "soundON";
        console.log("Turned ON");
    }
}

function createAudioObj(fileName) {
    let audio = document.createElement("audio");
    audio.src = "assets/sounds/" + fileName;
    return audio;
}