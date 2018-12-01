"use strict";

let ready = function () {
    let eb = new EventBus("http://localhost:8016/tetris-16/socket");

    eb.onopen = function () {
        console.log("Connection Open");

        console.warn("tetris-16.socket.server." + localStorage.getItem("game-address"));
    };

    function sendReady() {
        let data = {
            "session": cookies.getCookie("vertx-web.session")
        };

        const DONE_FUNC = function(err, reply) {
            console.warn("Ready: reply: " + JSON.stringify(reply));
        };

        eb.send("tetris-16.socket.server." + localStorage.getItem("game-address"), data, DONE_FUNC);
    }

    eb.onclose = function () {
        console.log("Connection Closed");
    };

    return {"sendReadyStatus": sendReady};
}();

