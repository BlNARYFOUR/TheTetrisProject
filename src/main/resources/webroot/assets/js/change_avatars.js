"use strict";

document.addEventListener("DOMContentLoaded", showAllImagesOfASpecificPage);

let elementSelectables;
let selectedItem;
let avatars = ["Banana", "Heart", "Standard", "TRex", "Triforce"];

function showAllImagesOfASpecificPage() {
    document.getElementById("yes").disabled = true;
    document.getElementById("no").disabled = true;

    document.getElementById("back").addEventListener("click", goBack);
    document.getElementById("save").addEventListener("click", saveAvatar);

    let location = document.getElementById("showAllAvatars");
    let imgList = "";
    let firstSelected = " selected";

    for (let i = 0; i < avatars.length; i++){
        imgList += "<figure id='" + avatars[i] + "' class='selectable" + firstSelected + "'><img data-avatarname='" + avatars[i] + "' src='assets/media/avatars/Avatar_" + avatars[i] + ".png' class='avatars'/></figure>";

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

        // Hier wordt de naam van de geselecteerde avatar ingestoken
        selectedItem = e.target.dataset.avatarname;

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

    location.href="main_menu.html";
    //TODO verander de avatar naar de gekozen avatar
    
}

function confirmNo(e) {
    e.preventDefault();

    document.getElementById("back").disabled = false;
    document.getElementById("save").disabled = false;
    document.getElementById("yes").disabled = true;
    document.getElementById("no").disabled = true;

    document.getElementById("confirm").classList.remove("show");
    document.getElementById("confirm").classList.add("hidden");

    document.getElementById(selectedItem).classList.remove("selected");
    document.getElementById(avatars[0]).classList.add("selected");

    selectedItem = null;

}