"use strict";

let eb = new EventBus("http://localhost:8081/tetris/events");

let userInfo;
let rewardsInfo;


eb.onopen = function () {
    console.log("Connection Open");

    //send sessionInfo to backend
    eb.registerHandler("tetris.events.sessionInfo", session());
    
    // give the rewards to javascript
    eb.registerHandler("tetris.events.rewards", function(err, message){
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
                console.log("test");
                eb.registerHandler("tetris.events.scratchCard", function(err, message){
                    console.log("boe");
                    scratchCard(message);
                });
                break;
            case "mystery box":
                //TODO
                break;
            default:
                console.log("something went wrong, we dont have this reward");
        }
    });
};

function session(){
    let obj = new Object();
    obj.session = getCookie("vertx-web.session");
    let json = JSON.stringify(obj);
    console.log(json);

    eb.send("tetris.events.sessionInfo", json);
}





eb.onclose = function () {
    console.log("Connection Closed");
};

