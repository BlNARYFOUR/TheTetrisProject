"use strict";

/*eb.onopen = function () {
    console.log("Connection Open");

    eb.registerHandler("tetris.events.gamemodes", testHandler);
};

function sendSessionInfo() {
    let data = {"session": getCookie("vertx-web.session")};

    const DONE_FUNC = function(err, reply) {
        console.log("sendSessionInfo: message has been broadcast: " + JSON.stringify(reply));
    };

    eb.send("tetris.events.gamemodes", data, DONE_FUNC);
}

eb.onclose = function () {
    console.log("Connection Closed");
};


function testHandler(err, message) {
    console.log("received a test-message:" + JSON.stringify(message));
}*/

function rewardsAndUserInfo(message) {
    // rewards
    let json = message;
    let body = JSON.parse(json.body);
    console.log(body);
    rewardsInfo = JSON.parse(body.rewards);

//user
    userInfo = JSON.parse(body.user);

    init();
}

function init() {

    document.getElementById("userName").innerText = userInfo.username;
    console.log("user" + userInfo.username);

    document.getElementById("logOut").addEventListener("click", logOut);
    document.getElementById("chooseGamemode").addEventListener("click", chooseGamemode);
    document.getElementById("clan").addEventListener("click", clan);
    document.getElementById("shop").addEventListener("click", shop);
    document.getElementById("highScore").addEventListener("click", highScore);
    document.getElementById("buyCubes").addEventListener("click", buyCubes);

    showDailyRewards();

}

function logOut() {
    location.href="../index.html";
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

function getCookie(cname) {
    let name = cname + "=";
    let decodedCookie = decodeURIComponent(document.cookie);
    let ca = decodedCookie.split(';');

    for(let i = 0; i <ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
            let val = c.substring(name.length, c.length);

            return val.replace(/\u002B/g, " ");
        }
    }

    return "";
}