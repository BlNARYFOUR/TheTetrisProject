"use strict";

document.addEventListener("DOMContentLoaded", init);

let players = [{name:"Bryan", score:0}, {name:"Brend", score:0}, {name:"Dennis", score:0}, {name:"Reinbert", score:100}];

function init() {
    loadPlayers();
}

function loadPlayers() {
    let list = "";
    for (let i = 0; i < players.length; i++) {
        list += "<li><span class='playersName'>" + players[i].name + "</span><span class='playersScore'>" + players[i].score + "</li>";
    }

    document.getElementById("listOfPlayers").innerHTML = list;

}