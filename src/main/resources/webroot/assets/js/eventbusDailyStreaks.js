"use strict";

let userInfo;
let rewardsInfo;
let avatarInfo;

let eb = new EventBus("/tetris-16/socket/");

eb.onopen = function () {
    console.log("Connection Open");

    //send sessionInfo to backend
    eb.registerHandler("tetris.events.sessionInfo", session());

    // give the rewards to javascript
    //TODO CAN BE DONE WITH FETCH
    eb.registerHandler("tetris.events.rewards", function(err, message){
        console.log("Redbull");
        rewardsAndUserInfo(message);

        let streakDays = (userInfo.streakDays) - 1;
        console.log("sd " + streakDays);
        console.log("reward " + rewardsInfo[streakDays].reward);
        switch (rewardsInfo[streakDays].reward){
            case "xp":
                // do nothing;
                console.log("xp");
                break;
            case "scratch card":
                console.log("scratch card");
                eb.registerHandler("tetris.events.scratchCard", function(err, message){
                    console.log("boe");
                    scratchCard(message);
                });
                break;
            case "mystery box":
                console.log("mystery box");
                eb.registerHandler("tetris.events.showMysteryBox", function (err, message) {
                    mysteryBox(message);
                });
                break;
            default:
                eb.onclose = function () {
                    console.log("Connection Closed");
                };

                console.log("something went wrong, we dont have this reward");
        }
    });
};

function session(){
    let obj = new Object();
    obj.session = cookies.getCookie("vertx-web.session");
    let json = JSON.stringify(obj);
    console.log(json);

    eb.send("tetris.events.sessionInfo", json);
}

eb.onclose = function () {
    console.log("Connection Closed");
};






