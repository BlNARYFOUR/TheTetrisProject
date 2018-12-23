"use strict";

let userInfo;
let rewardsInfo;
let avatarInfo;

let eb = new EventBus("/tetris-16/socket/");

eb.onopen = function () {
    console.log("Connection Open");

    // give the rewards to javascript
    eb.registerHandler("tetris.events.rewards", function(err, message){
        rewardsAndUserInfo(message);
        showDailyRewards();
        mainmenu();

        let streakDays = (userInfo.streakDays) - 1;
        switch (rewardsInfo[streakDays].reward){
            case "xp":
                // do nothing;
                console.log("xp");
                break;
            case "scratch card":
                console.log("scratch card");
                eb.registerHandler("tetris.events.scratchCard", function(err, message){
                    scratchCard(message);
                    showScratchCard();
                });
                break;
            case "mystery box":
                console.log("mystery box");
                eb.registerHandler("tetris.events.showMysteryBox", function (err, message) {
                    mysteryBox(message);
                    showMysteryBox();
                });
                break;
            default:
                eb.onclose = function () {
                    console.log("Connection Closed");
                };

                console.log("something went wrong, we dont have this reward");
        }
    });

    //send sessionInfo to backend
    session();
};

function session(){
    let json = { session: cookies.getCookie("vertx-web.session")};
    eb.send("tetris.events.sessionInfo", json);
}








function rewardsAndUserInfo(message) {
    // rewards
    let json = message;
    let body = JSON.parse(json.body);
    console.log(body);

    rewardsInfo = JSON.parse(body.rewards);

    //user
    userInfo = JSON.parse(body.user);

    //avatar
    avatarInfo = JSON.parse(body.avatar);
    mainmenu();
}

function mainmenu() {

    console.log("Hier ben ik");
    document.getElementById("userName").innerText = userInfo.username;
    document.querySelector(".numberOfCubes").innerHTML = userInfo.cubes;
    document.querySelector(".clanpoints").innerHTML = userInfo.clanPoints;

    document.getElementById("changeAvatar").innerHTML =
        "<img src='/static/assets/media/avatars/Avatar_" + avatarInfo.name + ".png' class='user-avatar' alt='avatar' title='avatar'>";

    UserInClan();

    generateLevelAndXP();

    document.getElementById("chooseGamemode").addEventListener("click", chooseGamemode);
    document.getElementById("clan").addEventListener("click", clan);
    document.getElementById("shop").addEventListener("click", shop);
    document.getElementById("highScore").addEventListener("click", highScore);
    document.getElementById("buyCubes").addEventListener("click", buyCubes);
    document.getElementById("changeAvatar").addEventListener("click", changeAvatar);
}
function UserInClan() {
    //TODO let claninfo be generate in this function and not in html
    if (!userInfo.hasAClan) {
        document.querySelector(".userInfo-2").style.visibility = "hidden";
    }
}

function generateLevelAndXP() {
    console.log("xp bitch");
    let lvl = 1;
    let xp = userInfo.xp;
    let neededXPForNextLvl = 200;

    if (xp < neededXPForNextLvl){
        lvl = 1;

    } else {
        for (neededXPForNextLvl; xp >= neededXPForNextLvl; neededXPForNextLvl += 200) {
            xp -= neededXPForNextLvl;
            lvl += 1;
        }
    }

    xpBar(xp, neededXPForNextLvl);
    document.querySelector(".level").innerHTML = lvl;
    document.querySelector(".xp-text").innerHTML = xp + " / " + neededXPForNextLvl + " xp";

}

function xpBar(xp, neededXPForNetxtLvl) {
    let bar = (xp / neededXPForNetxtLvl) * 100;
    document.querySelector(".xp-value").style.width = bar+"%";
}

function chooseGamemode() {
    location.href = "chooseGamemode.html";
}

function clan() {
    alert("Will be available in the future!");
}

function shop() {
    alert("Will be available in the future!");
}

function highScore() {
    alert("Will be available in the future!");
}

function buyCubes() {
    alert("Will be available in the future! \n" +
        "(At this moment you can not spend money, we think about your wallet)");
}

function changeAvatar() {
    eb.onclose = function () {
        console.log("Connection Closed");
    };

    location.href = "/static/pages/change_avatar.html";
}