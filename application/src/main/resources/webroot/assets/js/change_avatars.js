"use strict";

document.addEventListener("DOMContentLoaded", showAllImagesOfASpecificPage);

let elementSelectables;
let selectedItem = null;

function showAllImagesOfASpecificPage() {
    document.getElementById("yes").disabled = true;
    document.getElementById("no").disabled = true;

    document.getElementById("back").addEventListener("click", goBack);
    document.getElementById("save").addEventListener("click", saveAvatar);

    let location = document.getElementById("showAllAvatars");
    let imgList = "";
    let firstSelected = " selected";

    for (let i = 0; i < 4; i++){
        imgList += "<figure class='selectable" + firstSelected + "'><img data-avatarid='" + i + "' src='../assets/media/retroBlocks.png' class='avatars'/></figure>";

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

function saveAvatar() {
    console.log("disabled back and save");
    document.getElementById("back").disabled = true;
    document.getElementById("save").disabled = true;
    document.getElementById("yes").disabled = false;
    document.getElementById("no").disabled = false;

    document.getElementById("confirm").classList.remove("hidden");
    document.getElementById("confirm").classList.add("show");

    document.getElementById("yes").addEventListener("click", confirmYes);
    document.getElementById("no").addEventListener("click", confirmNo);
}

function confirmYes(e) {
    e.preventDefault();
    console.log("testid");

    location.href="main_menu.html";
    //TODO verander de avatar naar de gekozen avatar

}

function confirmNo() {
    document.getElementById("back").disabled = false;
    document.getElementById("save").disabled = false;
    document.getElementById("yes").disabled = true;
    document.getElementById("no").disabled = true;

    document.getElementById("confirm").classList.remove("show");
    document.getElementById("confirm").classList.add("hidden");

    //TODO de eerste avatar selecteren
    //TODO selectedItem terug op null

}