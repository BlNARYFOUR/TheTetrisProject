"use strict";

document.addEventListener("DOMContentLoaded", init);

let elementSelectables;
let selectedHero;

const SOUNDS = {
    pikachu: createAudioObj("pikachu.mp3"),
    donkeyKong: createAudioObj("donkeykong.wav")
};

function init() {
    document.getElementById("back").addEventListener("click", goBack);
    document.getElementById("playGame").addEventListener("click", play);

    console.log("testid");

    let xmlhttp = new XMLHttpRequest();
    let url = "/tetris-16/api/getHeroes";

    xmlhttp.onreadystatechange = function(){
        if (this.readyState == 4 && this.status == 200){
            let myArr = JSON.parse(this.responseText);
            console.log(myArr);
            loadHeroes(myArr);
        }
    };
    xmlhttp.open("POST", url, true);
    xmlhttp.send();

   //loadHeroes();


}

function createAudioObj(fileName) {
    let audio = document.createElement("audio");
    audio.src = "assets/sounds/" + fileName;
    return audio;
}

function playSounds(hero) {
    if (hero === "pikachu") {
        SOUNDS.pikachu.play();
    } else if (hero === "Donkey_Kong") {
        SOUNDS.donkeyKong.play();
    }
}

function loadHeroes(arr) {

    let location = document.getElementById("choose-hero");
    let imgList = "";
    let heroes = [];

    let firstSelected = " selected";

    for (let i = 0; i < arr.length; i++) {
        heroes.push(arr[i].heroName);

        imgList += "<li id='heroes' class='selectable hero-" + heroes[i] + firstSelected + "' data-heroname='" + heroes[i] + "'>" +
            "<img data-heroname='" + heroes[i] + "' src=assets/media/" + heroes[i] + ".gif class='"+ heroes[i] +"' title='"+ heroes[i] +"' alt='"+ heroes[i] +"'>" +
            "<p data-heroname='" + heroes[i] + "'>"+ heroes[i] +"</p>" +
            "</li>";

        firstSelected = "";
    }

    location.innerHTML = imgList;
    selectedHero = heroes[0];
    storeHero();
    selectablesEvents();
}

function selectablesEvents(){
    elementSelectables = document.getElementsByClassName("selectable");

    for (let i = 0; i < elementSelectables.length; i++) {
        elementSelectables[i].addEventListener("click", changeSelected);
    }
}

function changeSelected(e) {
    e.stopPropagation();
    if(e.target.parentNode.classList.contains("selectable")){
        for (let i = 0; i < elementSelectables.length; i++) {
            elementSelectables[i].classList.remove("selected");
        }
        e.target.parentNode.classList.add("selected");

        selectedHero = e.target.dataset.heroname;

        playSounds(selectedHero);
        storeHero();
        retrieveHero();
    }
    e.target.parentNode.classList.add("selected");

    console.log(e.target.getAttribute("data-heroname"));
    if (e.target.tagName === "MAIN" || e.target.tagName === "UL" || e.target.tagName === "LI"){
        e.target.parentNode.classList.remove("selected");
    }

}


function goBack(e) {
    location.href = "chooseGamemode.html"; //Go to gamemode
}

function play(e) {
    console.log("Waiting for matchmaking...");
    matching.sendMatchRequest();
}

function isLocalStorageSupported() {
    try{
        return "localStorage" in window && window["localStorage"] !== null;
    }catch (e){
        return false;
    }
}

function storeHero() {
    if (isLocalStorageSupported()){
        localStorage.setItem("hero", selectedHero);
    } else {
        console.log("something went wrong!");
    }
}

function retrieveHero() {
    if (isLocalStorageSupported()){
        let heroAsString = localStorage.getItem("hero");
    } else {
        console.log("Somthing went wrong!");
    }
}

function emptyLocalStorage() {
    try{
        return "localStorage" in window && window["localStorage"] !== null;
    }catch(e) {
        return false;
    }
}