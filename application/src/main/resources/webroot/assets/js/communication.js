"use strict";

let communication = function () {
    let eb = new EventBus("http://localhost:8016/tetris-16/socket");

    let data = null;

    eb.onopen = function () {
        console.log("Connection Open");
        console.warn("tetris-16.socket.server.ready." + localStorage.getItem("game-address"));

        eb.registerHandler("tetris-16.socket.client.game." + cookies.getCookie("game-address"), gameHandler);
    };

    let gameHandler = function (err, message) {
        //console.log("received a matching message:" + JSON.stringify(message));

        data = JSON.parse(message.body);
    };

    function resolveOnOpenState() {
        return new Promise(resolve => {
            const FUNC = resolve => {
                if (eb.state === EventBus.OPEN) {
                    resolve(true);
                }
                setTimeout(() => {
                    FUNC(resolve)
                }, 1);
            };

            FUNC(resolve);
        });
    }

    async function sendReady() {
        await resolveOnOpenState();

        let data = {
            "session": cookies.getCookie("vertx-web.session")
        };

        const DONE_FUNC = function(err, reply) {
            console.warn("Ready: reply: " + JSON.stringify(reply));
        };

        eb.send("tetris-16.socket.server.ready." + localStorage.getItem("game-address"), data, DONE_FUNC);
        console.warn("SEND READY");
    }

    eb.onclose = function () {
        console.log("Connection Closed");
    };

    return {"sendReadyStatus": sendReady, "gameData": data};
}();

