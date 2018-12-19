"use strict";

let matching = function () {
    let eb = new EventBus("/tetris-16/socket/");

    eb.onopen = function () {
        console.log("Connection Open");

        console.warn("tetris.client.match." + cookies.getCookie("vertx-web.session"));
        eb.registerHandler("tetris.client.match." + cookies.getCookie("vertx-web.session"), matchHandler);
    };

    function sendMatchRequest() {
        let data = {
            "session": cookies.getCookie("vertx-web.session"),
            "gameMode": localStorage.getItem("gameMode"),
            "hero": localStorage.getItem("hero")
        };

        const DONE_FUNC = function(err, reply) {
            console.log("Match: reply: " + JSON.stringify(reply));
        };

        eb.send("tetris.server.match", data, DONE_FUNC);
    }

    eb.onclose = function () {
        console.log("Connection Closed");
    };

    let matchHandler = function (err, message) {
        console.log("received a matching message:" + JSON.stringify(message));

        let data = JSON.parse(message.body);

        console.log("body: " + JSON.stringify(data["match"]));
        localStorage.setItem("game-address", data["match"]);
        localStorage.setItem("amountOfPlayers", data["amountOfPlayers"]);
        location.href = "tetrisMultiplayerField.html";
    };

    return {"sendMatchRequest": sendMatchRequest};
}();

