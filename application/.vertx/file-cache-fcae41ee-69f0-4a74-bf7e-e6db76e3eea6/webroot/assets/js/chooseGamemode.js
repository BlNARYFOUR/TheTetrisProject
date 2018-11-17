"use strict";

document.addEventListener("DOMContentLoaded", showAllGamemodes);

let elementSelectables;
let selectedGamemode;

function showAllGamemodes() {

    document.getElementById("back").addEventListener("click", goBack);

    let location = document.getElementById("showAllGamemodes");
    let imgList = "";
    let gamemodes = ["single_player", "multi_player", "time_attack", "last_man_standing"];

    let firstSelected = " selected";

    for (let i = 0; i < gamemodes.length; i++){
        imgList += "<figure class='selectable" + firstSelected + "'><img data-gamemodename='" + gamemodes[i] + "' src='../assets/media/gamemodes/" + gamemodes[i] + ".png' alt='" + gamemodes[i] + "' title='" + gamemodes[i] + "' class='gamemodes'/></figure>";

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
    if(e.target.parentNode.classList.contains("selectable")){
        for (let i = 0; i < elementSelectables.length; i++) {
            elementSelectables[i].classList.remove("selected");
        }
        e.target.parentNode.classList.add("selected");

        // Hier wordt de naam van de geselecteerde gamemode ingestoken
        selectedGamemode = e.target.dataset.gamemodename;

    }
    e.target.parentNode.classList.add("selected");


    if (e.target.tagName === "FIGURE"){
        e.target.parentNode.classList.remove("selected");
    }

}

function goBack() {
    location.href="main_menu.html";
}