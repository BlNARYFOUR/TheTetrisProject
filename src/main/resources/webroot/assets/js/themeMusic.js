"use strict";

document.addEventListener("DOMContentLoaded", init);

const sounds = {
    themeMusic: createAudioObj("themeMusic.mp3")
};

function init() {
    sounds.loop = true;
    sounds.volume = 0.2;
    sounds.currentTime = 0;
    sounds.play();

    document.getElementById("toggleMusic").addEventListener("click", toggleMusic);
}

function toggleMusic() {
    if (document.getElementById("toggleMusic").className === "soundON"){
        sounds.themeMusic.muted = true;
        document.getElementById("toggleMusic").className = "soundOFF";
        console.log("turned OFF");
    } else {
        sounds.themeMusic.muted = false;
        document.getElementById("toggleMusic").className = "soundON";
        console.log("Turned ON");
    }
}

function createAudioObj(fileName) {
    let audio = document.createElement("audio");
    audio.src = "/static/assets/sounds/" + fileName;
    return audio;
}