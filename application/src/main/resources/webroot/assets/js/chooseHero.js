"use strict";

document.addEventListener("DOMContentLoaded", init);

let elementSelectables;

function init() {
    document.getElementById("back").addEventListener("click", goBack);
    loadHeroes();

}

function loadHeroes() {

    let location = document.getElementById("choose-hero");
    let imgList = "";
    let heroes = ["donkeykong", "donkeykong", "donkeykong", "donkeykong"];

    let firstSelected = " selected";

    for (let i = 0; i < heroes.length; i++) {
                imgList += "<li class='selectable hero-" + heroes[i] + firstSelected + "'><img src= ../assets/media/" + heroes[i] + ".gif class='"+ heroes[i] +"' title='"+ heroes[i] +"' alt='"+ heroes[i] +"'><p>"+ heroes[i] +"</p></li>";

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

    }
    e.target.parentNode.classList.add("selected");

    console.log(e.target.tagName);
    if (e.target.tagName === "MAIN" || e.target.tagName === "UL" || e.target.tagName === "LI"){
        e.target.parentNode.classList.remove("selected");
    }

}


function goBack() {
    location.href = "#"; //Go to gamemode
}