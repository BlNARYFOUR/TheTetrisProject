"use strict";

const GAME = {
    PLAYER_ID: -1,
    AMOUNT_OF_PLAYERS: 0,
    YOUR_GAME_BOARD: [],
    ENEMY_GAME_BOARDS: new Map()
};

let gameCommunication = function () {
    let eb = new EventBus("http://localhost:8016/tetris-16/socket");

    eb.onopen = function () {
        console.log("Connection Open");
        console.warn("tetris-16.socket.server.ready." + localStorage.getItem("game-address"));

        eb.registerHandler("tetris-16.socket.client.game." + localStorage.getItem("game-address"), gameHandler);
    };

    function getGameBoards() {
        let GAME_BOARDS = [];

        if(0 < GAME.YOUR_GAME_BOARD.length)
            GAME_BOARDS.push(GAME.YOUR_GAME_BOARD.slice());

        let enemyGameBoards = Array.from(GAME.ENEMY_GAME_BOARDS);

        for(let key in enemyGameBoards) {
            GAME_BOARDS.push(enemyGameBoards[key][1]);
        }

        return GAME_BOARDS;
    }

    let gameHandler = function (err, message) {
        //console.log("GameHandler: " + JSON.stringify(message));

        let data = JSON.parse(message.body);

        if(data["playerId"] === GAME.PLAYER_ID) {
            GAME.YOUR_GAME_BOARD = data["playingField"];
        } else {
            GAME.ENEMY_GAME_BOARDS.set(data["playerId"], data["playingField"]);
        }
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

        let d = {
            "session": cookies.getCookie("vertx-web.session")
        };

        const DONE_FUNC = function(err, reply) {
            console.warn("Ready: reply: " + JSON.stringify(reply));
            let data = JSON.parse(reply.body);

            GAME.PLAYER_ID = data["playerId"];
            GAME.AMOUNT_OF_PLAYERS = localStorage.getItem("amountOfPlayers");
        };

        eb.send("tetris-16.socket.server.ready." + localStorage.getItem("game-address"), d, DONE_FUNC);
        console.warn("SEND READY");
    }

    eb.onclose = function () {
        console.log("Connection Closed");
    };

    return {"sendReadyStatus": sendReady, "getGameBoards": getGameBoards};
}();

