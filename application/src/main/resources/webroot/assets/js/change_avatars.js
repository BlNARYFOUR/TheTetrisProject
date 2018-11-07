"use strict";

document.addEventListener("DOMContentLoaded", showAllImagesOfASpecificPage);

let elementSelectables;

function showAllImagesOfASpecificPage() {
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

}