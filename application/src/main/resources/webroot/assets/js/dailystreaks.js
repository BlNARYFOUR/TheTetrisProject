"use strict";

document.addEventListener("DOMContentLoaded", showDailyRewards);

let rewards = ["", "xp", "scratch card", "xp", "scratch card", "xp", "mystery box", "cubes"];
let amount = ["", 50, 1, 100, 1, 150, 1, 10];
let photo = "../assets/media/retroBlocks.png";
let days = 7;
let severalDaysLoggedIn = 3;

function showDailyRewards(e) {
    e.preventDefault();
    console.log("dailyRewards");

    document.getElementById("close").addEventListener("click", closeDailyStreaks);

    let location = document.getElementById("dailyRewards");
    let imgList = "";
    // todo bijhouden van elke hij al heeft ontvangen => Backend

    for (let i = 1; i < (days + 1); i++){

        if (rewards[i] === "cubes"){
            photo = "../assets/media/cube_logo.png";
        }

        if (severalDaysLoggedIn >= i){
            imgList += "<figure id=day_'" + i + "'>" +
                "<h2>Day " + i + "</h2>" +
                "<img data-dailysteak=day_'" + i + "_" + rewards[i] + "' src='" + photo + "' class='rewards'>" +
                "<i class='material-icons' id='doneMark'>done</i>" +
                "<figcaption>+ " + amount[i] + " " + rewards[i] + "</figcaption>" +
                "</figure>";
        } else {
            imgList += "<figure id=day_'" + i + "'>" +
                "<h2>Day " + i + "</h2>" +
                "<img data-dailysteak=day_'" + i + "_" + rewards[i] + "' src='" + photo + "' class='rewards'>" +
                "<figcaption>+ " + amount[i] + " " + rewards[i] + "</figcaption>" +
                "</figure>";
        }
        // dit kan later worden verwijdert (de regel hieronder) !!!!!!
        photo = "../assets/media/retroBlocks.png";
        //TODO src linken met naam van rewards
    }

    location.innerHTML = imgList;
}

function closeDailyStreaks(e) {
    e.preventDefault();

    document.getElementById("dailystreaks").classList.remove("showDailyRewards");
    document.getElementById("dailystreaks").classList.add("hidden");

}