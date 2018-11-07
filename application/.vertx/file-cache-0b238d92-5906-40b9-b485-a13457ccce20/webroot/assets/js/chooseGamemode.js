"use strict";

document.addEventListener("DOMContentLoaded", showAllGamemodes);

let elementSelectables;

function showAllGamemodes() {
    document.getElementById("back").addEventListener("click", goBack);

    let location = document.getElementById("showAllGamemodes");
    let imgList = "";

    let nameGamemode = "Testid";
    let firstSelected = " selected";

    for (let i = 0; i < 4; i++){
        imgList += "<figure class='selectable" + firstSelected + "'><img data-gameid='" + i + "' src='assets/media/retroBlocks.png'/><figcaption>" + nameGamemode + "</figcaption></figure>";

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

    if (e.target.tagName === "FIGURE"){
        e.target.parentNode.classList.remove("selected");
    }

}

function goBack() {
    location.href="main_menu.html";
}