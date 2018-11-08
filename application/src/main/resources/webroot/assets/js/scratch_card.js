"use strict";

document.addEventListener("DOMContentLoaded", showScratchCard);

let prices = ["cubes", "xp", "skin", "nothing"];
let boxes = 3;

function showScratchCard() {
    let location = document.getElementById("prices");
    let imgList = "";

    imgList += "<p>This can be in it</p>";

    for (let j = 0; j < prices.length; j++){
        imgList += "<figure id='" + prices[j] + "'>" +
            "<img data-prices='" + prices[j] + "' src='../assets/media/retroBlocks.png' class='prices' />" +
            "</figure>";
    }
    location.innerHTML = imgList;


    location = document.getElementById("scratch");
    let boxesList = "";

    for (let k = 0; k < boxes; k++){
        boxesList += "<figure id='box_" + boxes + "''>" +
            "<img data-box='box_" + boxes + "' src='../assets/media/retroBlocks.png' class='boxes' />" +
            "</figure>";
    }

    location.innerHTML = boxesList;

}
