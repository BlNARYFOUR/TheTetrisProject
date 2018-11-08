"use strict";

document.addEventListener("DOMContentLoaded", showDailyRewards);

let rewards = ["", "xp", "scratch card", "xp", "scratch card", "xp", "mystery box", "cubes"];
let amount = ["", 50, 1, 100, 1, 150, 1, 10];
let days = 7;

function showDailyRewards() {
    console.log("dailyRewards");

    let location = document.getElementById("dailyRewards");
    let imgList = "";
    // todo bijhouden van elke hij al heeft ontvangen => Backend

    for (let i = 1; i < (days + 1); i++){
        imgList += "<figure id=day_'" + i + "'>" +
            "<h2>Day " + i + "</h2>" +
            "<img data-dailysteak=day_'" + i + "_" + rewards[i] + "' src='../assets/media/retroBlocks.png' class='rewards'>" +
            "<figcaption>+ " + amount[i] + " " + rewards[i] + "</figcaption>" +
            "</figure>"
        //TODO src linken met naam van rewards
    }

    location.innerHTML = imgList;




}