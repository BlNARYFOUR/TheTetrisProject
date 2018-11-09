"use strict";

document.addEventListener("DOMContentLoaded", showDailyRewards);

let rewards = ["", "xp", "scratch_card", "xp", "scratch_card", "xp", "mystery_box", "cubes"];
let amount = ["", 50, 1, 100, 1, 150, 1, 10];
let photo = "../assets/media/retroBlocks.png";
let days = 7;
let severalDaysLoggedIn = 2;
let whitchRewardYouGet;

let alreadyLoggedIn = false;

function showDailyRewards(e) {
    e.preventDefault();

    if (alreadyLoggedIn === false){
        //disabledButtons();

        document.getElementById("close").addEventListener("click", closeDailyStreaks);

        let location = document.getElementById("dailyRewards");
        let imgList = "";
        // todo bijhouden van elke hij al heeft ontvangen => Backend

        for (let i = 1; i < (days + 1); i++){

            if (rewards[i] === "cubes"){
                photo = "../assets/media/cube_logo.png";
            } else if (rewards[i] === "xp"){
                photo = "../assets/media/retroBlocks.png";
            } else if (rewards[i] === "scratch card"){
                photo = "../assets/media/retroBlocks.png";
            } else if (rewards[i] === "mystery box"){
                photo = "../assets/media/retroBlocks.png";
            }

            if (severalDaysLoggedIn >= i){
                imgList += "<figure id='day_" + i + "'>" +
                    "<a href='#' id='click_" + rewards[i] + "'><h2>Day " + i + "</h2>" +
                    "<img data-dailysteak='day_" + i + "_" + rewards[i] + "' src='" + photo + "' class='rewards'>" +
                    "<i class='material-icons' id='doneMark'>done</i>" +
                    "<figcaption>+ " + amount[i] + " " + rewards[i] + "</figcaption></a>" +
                    "</figure>";

                    whitchRewardYouGet = rewards[severalDaysLoggedIn];
                    console.log("wryg " + whitchRewardYouGet);


            } else {
                imgList += "<figure id=day_'" + i + "'>" +
                    "<a href='#' id='click_" + rewards[i] + "'><h2>Day " + i + "</h2>" +
                    "<img data-dailysteak=day_'" + i + "_" + rewards[i] + "' src='" + photo + "' class='rewards'>" +
                    "<figcaption>+ " + amount[i] + " " + rewards[i] + "</figcaption></a>" +
                    "</figure>";
            }
        }

        location.innerHTML = imgList;

        alreadyLoggedIn = true;
    }else {
        //closeDailyStreaks(e);
    }

    switch (whitchRewardYouGet){
        case "scratch_card":
            document.getElementById("click_scratch_card").addEventListener("click", scratchCard);
            break;
        default:
            //TODO;
    }



}

function scratchCard(e) {
    e.preventDefault();

    showScratchCard();
    document.getElementById("scratchCard").classList.remove("hiddenDailyRewards");
    document.getElementById("scratchCard").classList.add("showDailyRewards");
    closeDailyStreaks(e);
}

function disabledButtons() {
    document.getElementById("logOut").disabled = true;
    document.getElementById("chooseGamemode").disabled = true;
    document.getElementById("clan").disabled = true;
    document.getElementById("shop").disabled = true;
    document.getElementById("highScore").disabled = true;
    document.getElementById("buyCubes").disabled = true;
    document.getElementById("close").disabled = false;
}


function closeDailyStreaks(e) {
    e.preventDefault();

    //enabledButtons();

    document.getElementById("dailystreaks").classList.remove("showDailyRewards");
    document.getElementById("dailystreaks").classList.add("hiddenDailyRewards");

}

function enabledButtons() {
    document.getElementById("logOut").disabled = false;
    document.getElementById("chooseGamemode").disabled = false;
    document.getElementById("clan").disabled = false;
    document.getElementById("shop").disabled = false;
    document.getElementById("highScore").disabled = false;
    document.getElementById("buyCubes").disabled = false;
    document.getElementById("close").disabled = true;
}

