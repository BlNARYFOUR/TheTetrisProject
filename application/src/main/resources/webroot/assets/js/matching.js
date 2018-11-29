"use strict";

let matching = function () {
    let eb = new EventBus("http://localhost:8016/tetris-16/socket");

    eb.onopen = function () {
        console.log("Connection Open");

        eb.registerHandler("tetris-16.socket.client.match." + cookies.getCookie(), matchHandler);
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

        eb.send("tetris-16.socket.server.match", data, DONE_FUNC);
    }

    eb.onclose = function () {
        console.log("Connection Closed");
    };

    let matchHandler = function (err, message) {
        console.log("received a matching message:" + JSON.stringify(message));
    };

    return {"sendMatchRequest": sendMatchRequest};
}();

