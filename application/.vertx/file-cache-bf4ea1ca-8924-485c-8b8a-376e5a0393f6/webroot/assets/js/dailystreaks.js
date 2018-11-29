"use strict";

let rewards = [];
let amount = [];
let days = [];
let amountDays;
let severalDaysLoggedIn;
let whitchRewardYouGet;
let alreadyLoggedIn;

function showDailyRewards() {

    // make array empty
    // TODO this can be deleted when backend send data in 1 time
    /*rewards.length = 0;
    amount.length = 0;
    days.length = 0;*/

    amountDays = rewardsInfo.length;
    let photo = "../assets/media/daily_streaks/retroBlocks.png";
    severalDaysLoggedIn = userInfo.streakDays;
    alreadyLoggedIn = userInfo.alreadyLoggedInToday;

    if (alreadyLoggedIn === false){
        disabledButtons();

        document.getElementById("close").addEventListener("click", closeDailyStreaks);
        let location = document.getElementById("dailyRewards");
        let imgList = "";

        for (let i = 0; i < amountDays; i++){

            days.push(rewardsInfo[i].day);
            amount.push(rewardsInfo[i].amount);
            rewards.push(rewardsInfo[i].reward);

            // TODO dit is momenteel totdat ik alle foto's heb dan maak ik 1 path
            if (rewards[i] === "cubes"){
                photo = "../assets/media/daily_streaks/cubes.png";
            } else if (rewards[i] === "xp"){
                photo = "../assets/media/daily_streaks/xp.png";
            } else if (rewards[i] === "scratch card"){
                photo = "../assets/media/daily_streaks/retroBlocks.png";
            } else if (rewards[i] === "mystery box"){
                photo = "../assets/media/daily_streaks/retroBlocks.png";
            }

            if (severalDaysLoggedIn >= days[i]){
                imgList += "<figure id='day_" + days[i] + "'>" +
                    "<a href='#' id='click_" + rewards[i].replace(' ', '_') + "_day_" + days[i] + "'><h2>Day " + days[i] + "</h2>" +
                    "<img data-dailysteak='day_" + days[i] + "_" + rewards[i].replace(' ', '_') + "' src='" + photo + "' class='rewards'>" +
                    "<i class='material-icons' id='doneMark'>done</i>" +
                    "<figcaption>+ " + amount[i] + " " + rewards[i] + "</figcaption></a>" +
                    "</figure>";

                console.log(rewards);
                whitchRewardYouGet = rewards[severalDaysLoggedIn - 1];
                console.log("reward i get today " + whitchRewardYouGet);


            } else {
                imgList += "<figure id=day_'" + days[i] + "'>" +
                    "<a href='#' id='click_" + rewards[i].replace(' ', '_') + "'><h2>Day " + days[i] + "</h2>" +
                    "<img data-dailysteak=day_'" + days[i] + "_" + rewards[i].replace(' ', '_') + "' src='" + photo + "' class='rewards'>" +
                    "<figcaption>+ " + amount[i] + " " + rewards[i] + "</figcaption></a>" +
                    "</figure>";
            }
        }

        location.innerHTML = imgList;
        alreadyLoggedIn = true;
    }else {
        closeDailyStreaks();
    }


    switch (whitchRewardYouGet){
        case "scratch card":
            document.getElementById("click_scratch_card_day_" + severalDaysLoggedIn).addEventListener("click", sendScratchCard);
            break;
        case "xp":
            document.getElementById("click_xp_day_" + severalDaysLoggedIn).addEventListener("click", receiveXPOrCubes);
            break;
        case "mystery box":
            document.getElementById("click_mystery_box_day_" + severalDaysLoggedIn).addEventListener("click", mysteryBox);
            break;
        case "cubes":
            document.getElementById("click_cubes_day_" + severalDaysLoggedIn).addEventListener("click", receiveXPOrCubes);
            break;


        default:
            //TODO;
    }
}

function sendScratchCard(e) {
    e.preventDefault();

    let obj = new Object();
    obj.reward = rewards[severalDaysLoggedIn - 1];
    let json = JSON.stringify(obj);

    eb.send("tetris.events.reward", json);

}

function receiveXPOrCubes(e) {
    e.preventDefault();
    let obj = new Object();
    obj.reward = rewards[severalDaysLoggedIn - 1];
    obj.amount = amount[severalDaysLoggedIn - 1];
    obj.alreadyLoggedInToday = true;

    let json = JSON.stringify(obj);

    alert("You reveive " + amount[severalDaysLoggedIn - 1] + " " + rewards[severalDaysLoggedIn - 1]);
    eb.send("tetris.events.reward", json);

    document.getElementById("dailystreaks").classList.remove("showDailyRewards");
    document.getElementById("dailystreaks").classList.add("hiddenDailyRewards");

    enabledButtons();

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


function closeDailyStreaks() {

    enabledButtons();

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
