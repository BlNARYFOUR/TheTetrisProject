"use strict";

document.addEventListener("DOMContentLoaded", init);

const GAME_MODES =  [
    "singlePlayer",
    "standard",
    "lastManStanding",
    "timeAttack"
];

let elementSelectables;
let selectedGamemode;

function init(e) {
    showAllGamemodes();

}

function showAllGamemodes() {
    document.getElementById("back").addEventListener("click", goBack);

    let location = document.getElementById("showAllGamemodes");
    let imgList = "";

    let firstSelected = " selected";

    for (let i = 0; i < GAME_MODES.length; i++){
        imgList += "<figure class='selectable" + firstSelected + "'><img data-gamemodename='" + GAME_MODES[i] + "' src='../assets/media/gamemodes/" + GAME_MODES[i] + ".png' alt='" + GAME_MODES[i] + "' title='" + GAME_MODES[i] + "' class='gamemodes'/></figure>";

        firstSelected = "";
    }

    location.innerHTML = imgList;
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

    let gameMode = e.target.dataset.gamemodename;
    console.log("Gamemode:", gameMode);
    localStorage.setItem("gameMode", gameMode);

    if(e.target.parentNode.classList.contains("selectable")){
        for (let i = 0; i < elementSelectables.length; i++) {
            elementSelectables[i].classList.remove("selected");
        }
        e.target.parentNode.classList.add("selected");
    }
    e.target.parentNode.classList.add("selected");


    if (e.target.tagName === "FIGURE"){
        e.target.parentNode.classList.remove("selected");
    }

}

function goBack(e) {
    e.preventDefault();
    location.href="main_menu.html";
}

function goNext(e) {

}