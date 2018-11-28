"use strict";

document.addEventListener("DOMContentLoaded", init);

let elementSelectables;
let selectedHero;

function init() {
    document.getElementById("back").addEventListener("click", goBack);
    loadHeroes();

}

function loadHeroes() {

    let location = document.getElementById("choose-hero");
    let imgList = "";
    let heroes = ["donkeykong"];

    let firstSelected = " selected";

    for (let i = 0; i < heroes.length; i++) {
        imgList += "<li class='selectable hero-" + heroes[i] + firstSelected + "' data-heroname='" + heroes[i] + "'>" +
            "<img data-heroname='" + heroes[i] + "' src= ../assets/media/" + heroes[i] + ".gif class='"+ heroes[i] +"' title='"+ heroes[i] +"' alt='"+ heroes[i] +"'>" +
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

        storeHero();
        retrieveHero();
    }
    e.target.parentNode.classList.add("selected");

    console.log(e.target.getAttribute("data-heroname"));
    if (e.target.tagName === "MAIN" || e.target.tagName === "UL" || e.target.tagName === "LI"){
        e.target.parentNode.classList.remove("selected");
    }

}


function goBack() {
    location.href = "#"; //Go to gamemode
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
        console.log(heroAsString);
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