"use strict";

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

    init();
}


function init() {
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
}
function UserInClan() {
    //TODO let claninfo be generate in this function and not in html
    if (!userInfo.hasAClan) {
        document.querySelector(".userInfo-2").style.visibility = "hidden";
    }
}

function generateLevelAndXP() {
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
    location.href = "/static/pages/chooseGamemode.html";
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