"use strict";

document.addEventListener("DOMContentLoaded", init);

const sounds = {
    themeMusic: createAudioObj("themeMusic.mp3")
};

function init() {
    sounds.themeMusic.loop = true;
    sounds.themeMusic.volume = 0.2;
    sounds.themeMusic.currentTime = 0;
    playMusic();
    document.getElementById("toggleMusic").addEventListener("click", toggleMusic);
}

function playMusic() {
    sounds.themeMusic.play().catch(function() {
        console.log("<MUSIC ERROR> You need to interact with the DOM first.");
        setTimeout(playMusic, 500);
    });
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
    audio.src = "assets/sounds/" + fileName;
    return audio;
}