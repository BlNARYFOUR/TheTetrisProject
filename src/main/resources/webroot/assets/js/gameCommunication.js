"use strict";

const GAME = {
    PLAYER_ID: -1,
    AMOUNT_OF_PLAYERS: 0,
    YOUR_GAME_BOARD: [],
    ENEMY_GAME_BOARDS: new Map(),
    YOUR_SCORE: 0,
    ENEMY_SCORES: new Map(),
    YOU_IS_DEAD: false,
    ENEMY_ARE_DEAD: new Map(),
    ABILITY_COST: 1000
};

let gameOverPlayed = false;

let gameCommunication = function () {
    let eb = new EventBus("/tetris-16/socket/");

    eb.onopen = function () {
        console.log("Connection Open");
        console.warn("tetris.events.server.ready." + localStorage.getItem("game-address"));

        eb.registerHandler("tetris.events.client.game." + localStorage.getItem("game-address"), gameHandler);
    };

    function getGameBoards() {
        let GAME_BOARDS = [];

        if (0 < GAME.YOUR_GAME_BOARD.length)
            GAME_BOARDS.push(GAME.YOUR_GAME_BOARD.slice());

        let enemyGameBoards = Array.from(GAME.ENEMY_GAME_BOARDS);

        for (let key in enemyGameBoards) {
            GAME_BOARDS.push(enemyGameBoards[key][1]);
        }

        return GAME_BOARDS;
    }

    function getScores() {
        let SCORES = [];

        SCORES.push(GAME.YOUR_SCORE);

        let enemyScores = Array.from(GAME.ENEMY_SCORES);

        for (let key in enemyScores) {
            SCORES.push(enemyScores[key][1]);
        }

        return SCORES;
    }

    function getAbilityCost() {
        return GAME.ABILITY_COST;
    }

    let gameHandler = function (err, message) {
        //console.log("GameHandler: " + JSON.stringify(message));

        let data = JSON.parse(message.body);

        if (data["playerId"] === GAME.PLAYER_ID) {
            GAME.YOUR_GAME_BOARD = data["playingField"];
            GAME.YOUR_SCORE = data["score"];
            GAME.YOU_IS_DEAD = data["isDead"];
        } else {
            GAME.ENEMY_GAME_BOARDS.set(data["playerId"], data["playingField"]);
            GAME.ENEMY_SCORES.set(data["playerId"], data["score"]);
            GAME.ENEMY_ARE_DEAD.set(data["playerId"], data["isDead"]);
        }

        if (GAME.YOU_IS_DEAD) {
            console.log("You are dead!");
            document.getElementById("container-gameOver").style.display = 'flex';
            document.getElementById("gameOverScore").innerHTML = GAME.YOUR_SCORE;
            if (!gameOverPlayed) {
                SOUNDS.gameOver.loop = false;
                gameOverPlayed = true;
                sounds.themeMusic.pause();
                SOUNDS.gameOver.play();
            }
        }

        GAME.ABILITY_COST = data["abilityCost"];
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

        const DONE_FUNC = function (err, reply) {
            console.warn("Ready: reply: " + JSON.stringify(reply));
            let data = JSON.parse(reply.body);

            GAME.PLAYER_ID = data["playerId"];
            GAME.AMOUNT_OF_PLAYERS = localStorage.getItem("amountOfPlayers");
        };

        eb.send("tetris.events.server.ready." + localStorage.getItem("game-address"), d, DONE_FUNC);
        console.warn("SEND READY");
    }

    async function sendKey(key, isKeyDownState) {
        await resolveOnOpenState();

        let d = {
            "session": cookies.getCookie("vertx-web.session"),
            "key": key,
            "state": isKeyDownState
        };

        const DONE_FUNC = function (err, reply) {
            //console.warn("Ready: reply: " + JSON.stringify(reply));
        };

        eb.send("tetris.events.server.game." + cookies.getCookie("vertx-web.session"), d, DONE_FUNC);
        //console.warn("SEND READY");
    }

    eb.onclose = function () {
        console.log("Connection Closed");
    };

    return {
        "sendReadyStatus": sendReady,
        "getGameBoards": getGameBoards,
        "getScores": getScores,
        "getAbilityCost": getAbilityCost,
        "sendKey": sendKey
    };
}();

